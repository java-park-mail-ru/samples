package ru.mail.park.mechanics;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.avatar.Board;
import ru.mail.park.mechanics.avatar.GameUser;
import ru.mail.park.mechanics.avatar.MechanicPart;
import ru.mail.park.mechanics.internal.GameSessionService;
import ru.mail.park.mechanics.internal.MechanicsTimeService;
import ru.mail.park.mechanics.internal.Shuffler;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @author k.solovyev
 */
public class GameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    @NotNull
    private final Id<GameSession> sessionId;
    @NotNull
    private final GameUser first;
    @NotNull
    private final GameUser second;
    @NotNull
    private final Board board;
    @NotNull
    private final GameSessionService gameSessionService;

    public GameSession(@NotNull UserProfile user1,
                       @NotNull UserProfile user2,
                       @NotNull GameSessionService gameSessionService,
                       @NotNull MechanicsTimeService mechanicsTimeService,
                       @NotNull Shuffler shuffler) {
        this.gameSessionService = gameSessionService;
        this.sessionId = Id.of(ID_GENERATOR.getAndIncrement());
        this.first = new GameUser(user1, mechanicsTimeService);
        this.second =  new GameUser(user2, mechanicsTimeService);
        board = new Board(this, shuffler);

    }

    @NotNull
    public Id<GameSession> getSessionId() {
        return sessionId;
    }

    @NotNull
    public Board getBoard() {
        return board;
    }

    @NotNull
    public GameUser getEnemy(@NotNull Id<UserProfile> userId) {
        if (userId.equals(first.getUserId())) {
            return second;
        }
        if (userId.equals(second.getUserId())) {
            return first;
        }
        throw new IllegalArgumentException("Requested enemy for game but user not participant");
    }

    @NotNull
    public GameUser getFirst() {
        return first;
    }

    @NotNull
    public GameUser getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GameSession another = (GameSession) o;

        return sessionId.equals(another.sessionId);
    }

    @NotNull
    public List<GameUser> getPlayers() {
        return List.of(first, second);
    }

    public void terminateSession() {
        gameSessionService.forceTerminate(this, true);
    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }

    @Override
    public String toString() {
        return '[' +
                "sessionId=" + sessionId +
                ", first=" + first +
                ", second=" + second +
                ']';
    }

    public boolean tryFinishGame() {
        if (first.claimPart(MechanicPart.class).getScore() >= Config.SCORES_TO_WIN
                || second.claimPart(MechanicPart.class).getScore() >= Config.SCORES_TO_WIN) {
            gameSessionService.finishGame(this);
            return true;
        }
        return false;
    }
}
