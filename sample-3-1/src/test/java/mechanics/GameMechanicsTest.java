package mechanics;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.mail.park.mechanics.GameMechanics;
import ru.mail.park.mechanics.GameSession;
import ru.mail.park.mechanics.MechanicsExecutor;
import ru.mail.park.mechanics.avatar.MechanicPart;
import ru.mail.park.mechanics.base.ClientSnap;
import ru.mail.park.mechanics.base.Coords;
import ru.mail.park.mechanics.internal.GameSessionService;
import ru.mail.park.mechanics.internal.Shuffler;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;
import ru.mail.park.websocket.RemotePointService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Solovyev on 06/11/2016.
 */
@SuppressWarnings({"MagicNumber", "NullableProblems", "SpringJavaAutowiredMembersInspection"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class GameMechanicsTest {

    @SuppressWarnings("unused")
    @MockBean
    private RemotePointService remotePointService;
    @SuppressWarnings("unused")
    @MockBean
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

    @Before
    public void setUp () {
        when(remotePointService.isConnected(any())).thenReturn(true);
        user1 = accountService.addUser("user1", "", "");
        user2 = accountService.addUser("user2", "", "");

    }

    @Test
    public void simpleFiring() {
        final GameSession gameSession = startGame(user1.getId(), user2.getId());
        gameMechanics.addClientSnapshot(gameSession.getFirst().getUserId(), createClientSnap(25,true, Coords.of(350,110)));
        gameMechanics.addClientSnapshot(gameSession.getSecond().getUserId(), createClientSnap(40, true, Coords.of(70,70)));
        gameMechanics.gmStep(100);
        Assert.assertEquals(1, gameSession.getFirst().claimPart(MechanicPart.class).getScore());
        Assert.assertEquals(1, gameSession.getSecond().claimPart(MechanicPart.class).getScore());
    }

    @Test
    public void firingAtLastTest() {
        Mockito.doAnswer(invocationOnMock -> {
            List<?> board = invocationOnMock.getArgument(0);
            Collections.swap(board,0, 8);
            return null;
        }).when(shuffler).shuffle(any());
        final GameSession gameSession = startGame(user1.getId(), user2.getId());
        gameMechanics.addClientSnapshot(gameSession.getSecond().getUserId(), createClientSnap(25,true, Coords.of(750,750)));
        gameMechanics.gmStep(100);
        Assert.assertEquals(1, gameSession.getSecond().claimPart(MechanicPart.class).getScore());
    }

    @Test
    public void firingTooFastTest() {

        final GameSession gameSession = startGame(user1.getId(), user2.getId());
        gameMechanics.addClientSnapshot(gameSession.getSecond().getUserId(), createClientSnap(25,true, Coords.of(50,50)));
        gameMechanics.gmStep(50);
        gameMechanics.addClientSnapshot(gameSession.getSecond().getUserId(), createClientSnap(25,true, Coords.of(50,50)));
        gameMechanics.gmStep(50);
        gameMechanics.addClientSnapshot(gameSession.getSecond().getUserId(), createClientSnap(25,true, Coords.of(50,50)));
        gameMechanics.gmStep(50);
        Assert.assertEquals(1, gameSession.getSecond().claimPart(MechanicPart.class).getScore());
    }

    @Test
    public void gameStartedTest () {
        startGame(user1.getId(), user2.getId());
    }

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
        Assert.assertNotNull("Game session should be started on closest tick, but it didn't", gameSession);
        return gameSession;
    }


}

