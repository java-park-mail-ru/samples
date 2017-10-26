package ru.mail.park.mechanics.avatar;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.game.GameObject;
import ru.mail.park.mechanics.game.GamePart;
import ru.mail.park.mechanics.game.Snap;
import ru.mail.park.mechanics.internal.MechanicsTimeService;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;

import java.util.HashMap;
import java.util.Map;

public class GameUser extends GameObject {
    @NotNull
    private final UserProfile userProfile;

    public GameUser(@NotNull UserProfile userProfile, @NotNull MechanicsTimeService timeService) {
        this.userProfile = userProfile;
        addPart(MousePart.class, new MousePart());
        addPart(MechanicPart.class, new MechanicPart(timeService));
    }

    @NotNull
    public UserProfile getUserProfile() {
        return userProfile;
    }

    @NotNull
    public Id<UserProfile> getUserId() {
        return userProfile.getId();
    }

    @Override
    public @NotNull ServerPlayerSnap getSnap() {
        return ServerPlayerSnap.snapPlayer(this);
    }

    @SuppressWarnings("unused")
    public static class ServerPlayerSnap implements Snap<GameUser> {
        private Id<UserProfile> userId;

        Map<String, Snap<? extends GamePart>> gameParts;

        public Id<UserProfile> getUserId() {
            return userId;
        }

        public Map<String, Snap<? extends GamePart>> getGameParts() {
            return gameParts;
        }

        public void setUserId(Id<UserProfile> userId) {
            this.userId = userId;
        }

        @NotNull
        public static ServerPlayerSnap snapPlayer(@NotNull GameUser gameUser) {
            final ServerPlayerSnap serverPlayerSnap = new ServerPlayerSnap();
            serverPlayerSnap.userId = gameUser.getUserProfile().getId();
            serverPlayerSnap.gameParts = new HashMap<>();
            gameUser.getPartSnaps().forEach(part -> serverPlayerSnap.gameParts.put(part.getClass().getSimpleName(), part));
            return serverPlayerSnap;
        }
    }

    @Override
    public String toString() {
        return "GameUser{" +
                "userProfile=" + userProfile +
                '}';
    }
}
