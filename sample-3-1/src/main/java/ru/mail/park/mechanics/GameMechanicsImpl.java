package ru.mail.park.mechanics;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.mail.park.mechanics.base.ClientSnap;
import ru.mail.park.mechanics.internal.*;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;
import ru.mail.park.websocket.RemotePointService;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author k.solovyev
 */
@SuppressWarnings({"unused", "FieldMayBeFinal"})
@Service
public class GameMechanicsImpl implements GameMechanics {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMechanicsImpl.class);

    @NotNull
    private final AccountService accountService;

    @NotNull
    private final ClientSnapshotsService clientSnapshotsService;

    @NotNull
    private final ServerSnapshotService serverSnapshotService;

    @NotNull
    private final RemotePointService remotePointService;

    @NotNull
    private final PullTheTriggerService pullTheTriggerService;

    @NotNull
    private final GameSessionService gameSessionService;

    @NotNull
    private final MechanicsTimeService timeService;

    @NotNull
    private final GameTaskScheduler gameTaskScheduler;

    @NotNull
    private Set<Id<UserProfile>> playingUsers = new HashSet<>();

    @NotNull
    private ConcurrentLinkedQueue<Id<UserProfile>> waiters = new ConcurrentLinkedQueue<>();

    @NotNull
    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    @SuppressWarnings("LongLine")
    public GameMechanicsImpl(@NotNull AccountService accountService, @NotNull ClientSnapshotsService clientSnapshotsService, @NotNull ServerSnapshotService serverSnapshotService,
                             @NotNull RemotePointService remotePointService, @NotNull PullTheTriggerService pullTheTriggerService,
                             @NotNull GameSessionService gameSessionService, @NotNull MechanicsTimeService timeService, @NotNull GameTaskScheduler gameTaskScheduler) {
        this.accountService = accountService;
        this.clientSnapshotsService = clientSnapshotsService;
        this.serverSnapshotService = serverSnapshotService;
        this.remotePointService = remotePointService;
        this.pullTheTriggerService = pullTheTriggerService;
        this.gameSessionService = gameSessionService;
        this.timeService = timeService;
        this.gameTaskScheduler = gameTaskScheduler;
    }

    @Override
    public void addClientSnapshot(@NotNull Id<UserProfile> userId, @NotNull ClientSnap clientSnap) {
        tasks.add(() -> clientSnapshotsService.pushClientSnap(userId, clientSnap));
    }

    @Override
    public void addUser(@NotNull Id<UserProfile> userId) {
        if (gameSessionService.isPlaying(userId)) {
            return;
        }
        waiters.add(userId);
        if (LOGGER.isDebugEnabled()) {
            final UserProfile user = accountService.getUserById(userId);
            LOGGER.debug(String.format("User %s added to the waiting list", user.getLogin()));
        }
    }

    private void tryStartGames() {
        final Set<UserProfile> matchedPlayers = new LinkedHashSet<>();

        while (waiters.size() >= 2 || waiters.size() >= 1 && matchedPlayers.size() >= 1) {
            final Id<UserProfile> candidate = waiters.poll();
            if (!insureCandidate(candidate)) {
                continue;
            }
            matchedPlayers.add(accountService.getUserById(candidate));
            if (matchedPlayers.size() == 2) {
                final Iterator<UserProfile> iterator = matchedPlayers.iterator();
                gameSessionService.startGame(iterator.next(), iterator.next());
                matchedPlayers.clear();
            }
        }
        matchedPlayers.stream().map(UserProfile::getId).forEach(waiters::add);
    }

    private boolean insureCandidate(@NotNull Id<UserProfile> candidate) {
        return remotePointService.isConnected(candidate) &&
                accountService.getUserById(candidate) != null;
    }

    @Override
    public void gmStep(long frameTime) {
        while (!tasks.isEmpty()) {
            final Runnable nextTask = tasks.poll();
            if (nextTask != null) {
                try {
                    nextTask.run();
                } catch (RuntimeException ex) {
                    LOGGER.error("Can't handle game task", ex);
                }
            }
        }

        for (GameSession session : gameSessionService.getSessions()) {
            clientSnapshotsService.processSnapshotsFor(session);
        }

        gameTaskScheduler.tick();

        final Iterator<GameSession> iterator = gameSessionService.getSessions().iterator();
        final List<GameSession> sessionsToTerminate = new ArrayList<>();
        while (iterator.hasNext()) {
            final GameSession session = iterator.next();
            if (session.tryFinishGame()) {
                gameSessionService.forceTerminate(session, false);
                continue;
            }

            if (!gameSessionService.checkHealthState(session)) {
                gameSessionService.forceTerminate(session, true);
                continue;
            }

            try {
                serverSnapshotService.sendSnapshotsFor(session, frameTime);
            } catch (RuntimeException ex) {
                LOGGER.error("Failed to send snapshots, terminating the session", ex);
                sessionsToTerminate.add(session);
            }
            pullTheTriggerService.pullTheTriggers(session);
        }
        sessionsToTerminate.forEach(session -> gameSessionService.forceTerminate(session, true));

        tryStartGames();
        clientSnapshotsService.clear();
        timeService.tick(frameTime);
    }

    @Override
    public void reset() {

    }
}
