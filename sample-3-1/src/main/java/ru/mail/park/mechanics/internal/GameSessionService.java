package ru.mail.park.mechanics.internal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import ru.mail.park.mechanics.Config;
import ru.mail.park.mechanics.GameSession;
import ru.mail.park.mechanics.avatar.GameUser;
import ru.mail.park.mechanics.avatar.MechanicPart;
import ru.mail.park.mechanics.requests.FinishGame;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;
import ru.mail.park.websocket.RemotePointService;

import java.io.IOException;
import java.util.*;

@Service
public class GameSessionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSessionService.class);
    @NotNull
    private final Map<Id<UserProfile>, GameSession> usersMap = new HashMap<>();
    @NotNull
    private final Set<GameSession> gameSessions = new LinkedHashSet<>();

    @NotNull
    private final RemotePointService remotePointService;

    @NotNull
    private final MechanicsTimeService timeService;

    @NotNull
    private final GameInitService gameInitService;

    @NotNull
    private final GameTaskScheduler gameTaskScheduler;

    @NotNull
    private final ClientSnapshotsService clientSnapshotsService;

    @NotNull
    private final Shuffler shuffler;

    public GameSessionService(@NotNull RemotePointService remotePointService,
                              @NotNull MechanicsTimeService timeService,
                              @NotNull GameInitService gameInitService,
                              @NotNull GameTaskScheduler gameTaskScheduler,
                              @NotNull ClientSnapshotsService clientSnapshotsService,
                              @NotNull Shuffler shuffler) {
        this.remotePointService = remotePointService;
        this.timeService = timeService;
        this.gameInitService = gameInitService;
        this.gameTaskScheduler = gameTaskScheduler;
        this.clientSnapshotsService = clientSnapshotsService;
        this.shuffler = shuffler;
    }

    public Set<GameSession> getSessions() {
        return gameSessions;
    }

    @Nullable
    public GameSession getSessionForUser(@NotNull Id<UserProfile> userId) {
        return usersMap.get(userId);
    }

    public boolean isPlaying(@NotNull Id<UserProfile> userId) {
        return usersMap.containsKey(userId);
    }

    public void forceTerminate(@NotNull GameSession gameSession, boolean error) {
        final boolean exists = gameSessions.remove(gameSession);
        gameSession.setFinished();
        usersMap.remove(gameSession.getFirst().getUserId());
        usersMap.remove(gameSession.getSecond().getUserId());
        final CloseStatus status = error ? CloseStatus.SERVER_ERROR : CloseStatus.NORMAL;
        if (exists) {
            remotePointService.cutDownConnection(gameSession.getFirst().getUserId(), status);
            remotePointService.cutDownConnection(gameSession.getSecond().getUserId(), status);
        }
        clientSnapshotsService.clearForUser(gameSession.getFirst().getUserId());
        clientSnapshotsService.clearForUser(gameSession.getSecond().getUserId());

        LOGGER.info("Game session " + gameSession.getSessionId() + (error ? " was terminated due to error. " : " was cleaned. ")
                + gameSession.toString());
    }

    public boolean checkHealthState(@NotNull GameSession gameSession) {
        return gameSession.getPlayers().stream().map(GameUser::getUserId).allMatch(remotePointService::isConnected);
    }

    public void startGame(@NotNull UserProfile first, @NotNull UserProfile second) {
        final GameSession gameSession = new GameSession(first, second, this, timeService, shuffler);
        gameSessions.add(gameSession);
        usersMap.put(gameSession.getFirst().getUserId(), gameSession);
        usersMap.put(gameSession.getSecond().getUserId(), gameSession);
        gameSession.getBoard().randomSwap();
        gameInitService.initGameFor(gameSession);
        gameTaskScheduler.schedule(Config.START_SWITCH_DELAY, new SwapTask(gameSession, gameTaskScheduler, Config.START_SWITCH_DELAY));
        LOGGER.info("Game session " + gameSession.getSessionId() + " started. " + gameSession.toString());
    }

    public void finishGame(@NotNull GameSession gameSession) {
        gameSession.setFinished();
        final FinishGame.Overcome firstOvercome;
        final FinishGame.Overcome secondOvercome;
        final int firstScore = gameSession.getFirst().claimPart(MechanicPart.class).getScore();
        final int secondScore = gameSession.getSecond().claimPart(MechanicPart.class).getScore();
        if (firstScore == secondScore) {
            firstOvercome = FinishGame.Overcome.DRAW;
            secondOvercome = FinishGame.Overcome.DRAW;
        } else if (firstScore > secondScore) {
            firstOvercome = FinishGame.Overcome.WIN;
            secondOvercome = FinishGame.Overcome.LOSE;
        } else {
            firstOvercome = FinishGame.Overcome.LOSE;
            secondOvercome = FinishGame.Overcome.WIN;
        }

        try {
            remotePointService.sendMessageToUser(gameSession.getFirst().getUserId(), new FinishGame(firstOvercome));
        } catch (IOException ex) {
            LOGGER.warn(String.format("Failed to send FinishGame message to user %s",
                    gameSession.getFirst().getUserProfile().getLogin()), ex);
        }

        try {
            remotePointService.sendMessageToUser(gameSession.getSecond().getUserId(), new FinishGame(secondOvercome));
        } catch (IOException ex) {
            LOGGER.warn(String.format("Failed to send FinishGame message to user %s",
                    gameSession.getSecond().getUserProfile().getLogin()), ex);
        }
    }

    private static final class SwapTask extends GameTaskScheduler.GameSessionTask {

        private final GameTaskScheduler gameTaskScheduler;
        private final long currentDelay;

        private SwapTask(GameSession gameSession, GameTaskScheduler gameTaskScheduler, long currentDelay) {
            super(gameSession);
            this.gameTaskScheduler = gameTaskScheduler;
            this.currentDelay = currentDelay;
        }

        @Override
        public void operate() {
            if (getGameSession().isFinished()) {
                return;
            }
            getGameSession().getBoard().randomSwap();
            final long newDelay = Math.max(currentDelay - Config.SWITCH_DELTA, Config.SWITCH_DELAY_MIN);
            gameTaskScheduler.schedule(newDelay,
                    new SwapTask(getGameSession(), gameTaskScheduler, newDelay));
        }
    }

}
