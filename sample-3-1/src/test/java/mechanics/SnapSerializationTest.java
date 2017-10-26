package mechanics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.mail.park.mechanics.GameSession;
import ru.mail.park.mechanics.avatar.GameUser;
import ru.mail.park.mechanics.avatar.Board;
import ru.mail.park.mechanics.base.ClientSnap;
import ru.mail.park.mechanics.internal.MechanicsTimeService;
import ru.mail.park.mechanics.internal.Shuffler;
import ru.mail.park.mechanics.requests.InitGame;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;

import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("MagicNumber")
public class SnapSerializationTest {


    private GameUser nogibator;
    private GameUser papkaPro;
    private UserProfile pupkin;
    private UserProfile dudkin;

    @Before
    public void setUp() {
        pupkin = new UserProfile("Pupkin");
        dudkin = new UserProfile("Dudkin");
        nogibator = new GameUser(pupkin, new MechanicsTimeService());
        papkaPro = new GameUser(dudkin, new MechanicsTimeService());
    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    @Test
    public void clientSnapTest() throws IOException {
        final String clientSnapStr =
                    "{ " +

                        "\"mouse\":{" +
                            "\"x\":34.4," +
                            "\"y\":55.4" +
                        "}," +
                        "\"isFiring\":\"false\"," +
                        "\"class\":\"ClientSnap\"," +
                        "\"frameTime\":\"32\"" +
                    '}';
        final ObjectMapper objectMapper = new ObjectMapper();
        final ClientSnap clientSnap = objectMapper.readValue(clientSnapStr, ClientSnap.class);
        final String clientSnapJson = objectMapper.writeValueAsString(clientSnap);
        Assert.assertNotNull(clientSnapJson);
    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    @Test
    public void serverSnapTest() throws IOException {
        final GameUser.ServerPlayerSnap serverPlayerSnap = new GameUser.ServerPlayerSnap();
        serverPlayerSnap.setUserId(Id.of(4));
        final ObjectMapper objectMapper = new ObjectMapper();
        final String result = objectMapper.writeValueAsString(serverPlayerSnap);
        objectMapper.readValue(result, GameUser.ServerPlayerSnap.class);
    }

    @SuppressWarnings({"TooBroadScope", "OverlyBroadThrowsClause"})
    @Test
    public void serverInitTest() throws IOException {
        final InitGame.Request initGame = new InitGame.Request();
        final UserProfile pupkin = new UserProfile("Pupkin");
        final GameUser gameUser = new GameUser(pupkin, new MechanicsTimeService());
        final GameUser.ServerPlayerSnap serverPlayerSnap = this.nogibator.getSnap();
        final Map<Id<UserProfile>, String> names = ImmutableMap.of(
                this.nogibator.getUserId(), this.pupkin.getLogin(),
                this.papkaPro.getUserId(), this.dudkin.getLogin());
        final Map<Id<UserProfile>, String> colors = ImmutableMap.of(
                this.nogibator.getUserId(), "#aaaaaa",
                this.papkaPro.getUserId(), "#cccccc");
        final  Map<Id<UserProfile>, GameUser.ServerPlayerSnap> players = ImmutableMap.of(
                this.nogibator.getUserId(), this.nogibator.getSnap(),
                this.papkaPro.getUserId(), this.nogibator.getSnap()
        );
        initGame.setPlayers(players);
        initGame.setColors(colors);
        initGame.setSelf(gameUser.getUserId());
        initGame.setEnemy(gameUser.getUserId());
        initGame.setNames(names);
        initGame.setBoard(initSquare(this.nogibator, this.papkaPro).getSnap());
        final ObjectMapper objectMapper = new ObjectMapper();
        final String initGameJson = objectMapper.writeValueAsString(initGame);
        Assert.assertNotNull(initGameJson);
    }

    @Test
    public void snapTest() throws JsonProcessingException {
        final Board square = initSquare(this.nogibator, this.papkaPro);
        final ObjectMapper objectMapper = new ObjectMapper();
        final String squareJson = objectMapper.writeValueAsString(square.getSnap());
        Assert.assertNotNull(squareJson);
    }

    private Board initSquare(GameUser user1, GameUser user2) {
        final GameSession session = mock(GameSession.class);
        when(session.getFirst()).thenReturn(user1);
        when(session.getSecond()).thenReturn(user2);
        return new Board(session, mock(Shuffler.class));
    }


}
