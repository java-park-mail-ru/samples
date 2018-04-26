package ru.mail.park.mechanics;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.mail.park.mechanics.base.Coords;
import ru.mail.park.mechanics.game.MechanicPart;
import ru.mail.park.mechanics.messages.inbox.ClientSnap;
import ru.mail.park.mechanics.messages.outbox.FinishGame;
import ru.mail.park.mechanics.services.GameSessionService;
import ru.mail.park.mechanics.services.Shuffler;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;
import ru.mail.park.websocket.RemotePointService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by Solovyev on 06/11/2016.
 */
@SuppressWarnings({"MagicNumber", "NullableProblems", "SpringJavaAutowiredMembersInspection"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(SpringExtension.class)
class GameMechanicsTest {

    @MockBean
    private RemotePointService remotePointService;
    @MockBean // purposely replace real executor
    private MechanicsExecutor mechanicsExecutor;
    @MockBean
    private Shuffler shuffler;
    @Autowired
    private GameMechanics gameMechanics;
    @Autowired
    private AccountService accountService;
    @Autowired
    private GameSessionService gameSessionService;
    @NotNull
    private UserProfile user1;
    @NotNull
    private UserProfile user2;

    @BeforeEach
    void setUp() {
        when(remotePointService.isConnected(any())).thenReturn(true);
        user1 = accountService.addUser("user1");
        user2 = accountService.addUser("user2");
    }

    @AfterEach
    void tearDown() {
        gameSessionService.getSessions().forEach(session -> gameSessionService.forceTerminate(session, false));
    }

    @Test
    void simpleFiring() {
        final GameSession gameSession = startGame(user1.getId(), user2.getId());
        gameMechanics.addClientSnapshot(gameSession.getFirst().getUserId(), createClientSnap(25, true, Coords.of(1.5 * Config.SQUARE_SIZE, 0.5 * Config.SQUARE_SIZE)));
        gameMechanics.addClientSnapshot(gameSession.getSecond().getUserId(), createClientSnap(40, true, Coords.of(0.5 * Config.SQUARE_SIZE, 0.5 * Config.SQUARE_SIZE)));
        gameMechanics.gmStep(100);
        assertEquals(1, gameSession.getFirst().claimPart(MechanicPart.class).getScore());
        assertEquals(1, gameSession.getSecond().claimPart(MechanicPart.class).getScore());
    }

    @Test
    void firingAtLastTest() {
        Mockito.doAnswer(invocationOnMock -> {
            List<?> board = invocationOnMock.getArgument(0);
            Collections.swap(board, 0, 8);
            return null;
        }).when(shuffler).shuffle(any());
        final GameSession gameSession = startGame(user1.getId(), user2.getId());
        gameMechanics.addClientSnapshot(gameSession.getSecond().getUserId(), createClientSnap(25, true, Coords.of(Config.SQUARE_SIZE * 5 / 2, Config.SQUARE_SIZE * 5 / 2)));
        gameMechanics.gmStep(100);
        assertEquals(1, gameSession.getSecond().claimPart(MechanicPart.class).getScore());
    }

    @Test
    void firingTooFastTest() {

        final GameSession gameSession = startGame(user1.getId(), user2.getId());
        gameMechanics.addClientSnapshot(gameSession.getSecond().getUserId(), createClientSnap(25, true, Coords.of(0.5 * Config.SQUARE_SIZE, 0.5 * Config.SQUARE_SIZE)));
        gameMechanics.gmStep(50);
        gameMechanics.addClientSnapshot(gameSession.getSecond().getUserId(), createClientSnap(25, true, Coords.of(0.5 * Config.SQUARE_SIZE, 0.5 * Config.SQUARE_SIZE)));
        gameMechanics.gmStep(50);
        gameMechanics.addClientSnapshot(gameSession.getSecond().getUserId(), createClientSnap(25, true, Coords.of(0.5 * Config.SQUARE_SIZE, 0.5 * Config.SQUARE_SIZE)));
        gameMechanics.gmStep(50);
        assertEquals(1, gameSession.getSecond().claimPart(MechanicPart.class).getScore());
    }


    @Test
    void firingToWin() throws IOException {
        final GameSession gameSession = startGame(user1.getId(), user2.getId());

        final Runnable iteration = () -> {
            gameMechanics.addClientSnapshot(gameSession.getSecond().getUserId(), createClientSnap(Config.FIRING_COOLDOWN, true, Coords.of(0.5 * Config.SQUARE_SIZE, 0.5 * Config.SQUARE_SIZE)));
            gameMechanics.gmStep(Config.FIRING_COOLDOWN);
        };

        for (int i = 0; i < Config.SCORES_TO_WIN - 1; i++) {
            iteration.run();
        }
        final ArgumentCaptor<FinishGame> firstFinishMessage = ArgumentCaptor.forClass(FinishGame.class);
        final ArgumentCaptor<FinishGame> secondFinishMessage = ArgumentCaptor.forClass(FinishGame.class);
        final InOrder inOrder = inOrder(remotePointService);
        inOrder.verify(remotePointService, times(Config.SCORES_TO_WIN * 2)).sendMessageToUser(any(), any());  // init + 99 iterations
        iteration.run();
        inOrder.verify(remotePointService).sendMessageToUser(eq(gameSession.getFirst().getUserId()), firstFinishMessage.capture());
        inOrder.verify(remotePointService).sendMessageToUser(eq(gameSession.getSecond().getUserId()), secondFinishMessage.capture());

        assertEquals(firstFinishMessage.getValue().getOvercome(), FinishGame.Overcome.LOSE);
        assertEquals(secondFinishMessage.getValue().getOvercome(), FinishGame.Overcome.WIN);
        assertTrue(gameSession.isFinished());
        assertFalse(gameSessionService.getSessions().contains(gameSession));
        assertFalse(gameSessionService.isPlaying(gameSession.getFirst().getUserId()));
        assertFalse(gameSessionService.isPlaying(gameSession.getSecond().getUserId()));
        assertEquals(100, gameSession.getSecond().claimPart(MechanicPart.class).getScore());
    }

    @Test
    void firingToDraw() throws IOException {
        final GameSession gameSession = startGame(user1.getId(), user2.getId());

        final Runnable iteration = () -> {
            gameMechanics.addClientSnapshot(gameSession.getSecond().getUserId(), createClientSnap(Config.FIRING_COOLDOWN, true, Coords.of(0.5 * Config.SQUARE_SIZE, 0.5 * Config.SQUARE_SIZE)));
            gameMechanics.addClientSnapshot(gameSession.getFirst().getUserId(), createClientSnap(Config.FIRING_COOLDOWN, true, Coords.of(1.5 * Config.SQUARE_SIZE, 0.5 * Config.SQUARE_SIZE)));
            gameMechanics.gmStep(Config.FIRING_COOLDOWN);
        };

        for (int i = 0; i < Config.SCORES_TO_WIN - 1; i++) {
            iteration.run();
        }
        final ArgumentCaptor<FinishGame> firstFinishMessage = ArgumentCaptor.forClass(FinishGame.class);
        final ArgumentCaptor<FinishGame> secondFinishMessage = ArgumentCaptor.forClass(FinishGame.class);
        final InOrder inOrder = inOrder(remotePointService);
        inOrder.verify(remotePointService, times(Config.SCORES_TO_WIN * 2)).sendMessageToUser(any(), any());  // init + 99 iterations
        iteration.run();
        inOrder.verify(remotePointService).sendMessageToUser(eq(gameSession.getFirst().getUserId()), firstFinishMessage.capture());
        inOrder.verify(remotePointService).sendMessageToUser(eq(gameSession.getSecond().getUserId()), secondFinishMessage.capture());

        assertEquals(firstFinishMessage.getValue().getOvercome(), FinishGame.Overcome.DRAW);
        assertEquals(secondFinishMessage.getValue().getOvercome(), FinishGame.Overcome.DRAW);
        assertTrue(gameSession.isFinished());
        assertFalse(gameSessionService.getSessions().contains(gameSession));
        assertFalse(gameSessionService.isPlaying(gameSession.getFirst().getUserId()));
        assertFalse(gameSessionService.isPlaying(gameSession.getSecond().getUserId()));
        assertEquals(100, gameSession.getSecond().claimPart(MechanicPart.class).getScore());
    }

    @Test
    void gameStartedTest() {
        startGame(user1.getId(), user2.getId());
    }

    @SuppressWarnings("SameParameterValue")
    private ClientSnap createClientSnap(long frameTime, boolean firing, Coords mouse) {
        final ClientSnap clientSnap = new ClientSnap();
        clientSnap.setFrameTime(frameTime);
        clientSnap.setFiring(firing);
        clientSnap.setMouse(mouse);
        return clientSnap;
    }

    @NotNull
    private GameSession startGame(@NotNull Id<UserProfile> player1, @NotNull Id<UserProfile> player2) {
        gameMechanics.addUser(player1);
        gameMechanics.addUser(player2);
        gameMechanics.gmStep(0);
        @Nullable final GameSession gameSession = gameSessionService.getSessionForUser(player1);
        assertNotNull(gameSession, "Game session should be started on closest tick, but it didn't");
        return gameSession;
    }
}
