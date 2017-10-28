package ru.mail.park.mechanics.internal;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.mail.park.mechanics.GameSession;
import ru.mail.park.mechanics.avatar.GameUser;
import ru.mail.park.mechanics.avatar.MechanicPart;
import ru.mail.park.mechanics.avatar.MousePart;
import ru.mail.park.mechanics.base.ClientSnap;
import ru.mail.park.mechanics.base.Coords;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;

import java.util.*;

/**
 * Not thread safe! Per game mechanic service
 */
@Service
public class ClientSnapshotsService {

    private final Map<Id<UserProfile>, List<ClientSnap>> snaps = new HashMap<>();

    public void pushClientSnap(@NotNull Id<UserProfile> user, @NotNull ClientSnap snap) {
        this.snaps.putIfAbsent(user, new ArrayList<>());
        final List<ClientSnap> clientSnaps = snaps.get(user);
        clientSnaps.add(snap);
    }

    @NotNull
    public List<ClientSnap> getSnapForUser(@NotNull Id<UserProfile> user) {
        return snaps.getOrDefault(user, Collections.emptyList());
    }

    public void processSnapshotsFor(@NotNull GameSession gameSession) {
        final Collection<GameUser> players = new ArrayList<>();
        players.add(gameSession.getFirst());
        players.add(gameSession.getSecond());
        for (GameUser player : players) {
            final List<ClientSnap> playerSnaps = getSnapForUser(player.getUserId());
            if (playerSnaps.isEmpty()) {
                continue;
            }

            playerSnaps.stream().filter(ClientSnap::isFiring).findFirst().ifPresent(snap -> processClick(snap, gameSession, player));

            final ClientSnap lastSnap = playerSnaps.get(playerSnaps.size() - 1);
            processMouseMove(player, lastSnap.getMouse());
        }
    }

    private void processClick(@NotNull ClientSnap snap, @NotNull GameSession gameSession, @NotNull GameUser gameUser) {
        final MechanicPart mechanicPart = gameUser.claimPart(MechanicPart.class);
        if (mechanicPart.tryFire()) {
            gameSession.getBoard().fireAt(snap.getMouse());
        }
    }

    private void processMouseMove(@NotNull GameUser gameUser, @NotNull Coords mouse) {
        gameUser.claimPart(MousePart.class).setMouse(mouse);
    }

    public void clearForUser(Id<UserProfile> userProfileId) {
        snaps.remove(userProfileId);
    }

    public void reset() {
        snaps.clear();
    }
}
