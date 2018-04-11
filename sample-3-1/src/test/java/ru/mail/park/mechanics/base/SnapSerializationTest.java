package ru.mail.park.mechanics.base;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mail.park.mechanics.GameSession;
import ru.mail.park.mechanics.game.GameUser;
import ru.mail.park.mechanics.game.Board;
import ru.mail.park.mechanics.services.MechanicsTimeService;
import ru.mail.park.mechanics.services.Shuffler;
import ru.mail.park.mechanics.messages.inbox.ClientSnap;
import ru.mail.park.mechanics.messages.outbox.InitGame;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("MagicNumber")
public class SnapSerializationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private GameUser nogibator;
    private GameUser papkaPro;
    private UserProfile pupkin;
    private UserProfile dudkin;


    @BeforeEach
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

        final ClientSnap clientSnap = objectMapper.readValue(clientSnapStr, ClientSnap.class);
        final String clientSnapJson = objectMapper.writeValueAsString(clientSnap);
        assertNotNull(clientSnapJson);
    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    @Test
    public void serverSnapTest() throws IOException {
        final GameUser.ServerPlayerSnap serverPlayerSnap = new GameUser.ServerPlayerSnap();
        serverPlayerSnap.setUserId(Id.of(4));
        final String result = objectMapper.writeValueAsString(serverPlayerSnap);
        objectMapper.readValue(result, GameUser.ServerPlayerSnap.class);
    }

    @SuppressWarnings({"TooBroadScope", "OverlyBroadThrowsClause"})
    @Test
    public void serverInitTest() throws IOException {
        final InitGame.Request initGame = new InitGame.Request();
        final GameUser gameUser = new GameUser(pupkin, new MechanicsTimeService());
        assertNotNull(this.nogibator.getSnap());
        final Map<Id<UserProfile>, String> names = Map.of(
                this.nogibator.getUserId(), this.pupkin.getLogin(),
                this.papkaPro.getUserId(), this.dudkin.getLogin());
        final Map<Id<UserProfile>, String> colors = Map.of(
                this.nogibator.getUserId(), "#aaaaaa",
                this.papkaPro.getUserId(), "#cccccc");
        final  Map<Id<UserProfile>, GameUser.ServerPlayerSnap> players = Map.of(
                this.nogibator.getUserId(), this.nogibator.getSnap(),
                this.papkaPro.getUserId(), this.nogibator.getSnap()
        );
        initGame.setPlayers(players);
        initGame.setColors(colors);
        initGame.setSelf(gameUser.getUserId());
        initGame.setEnemy(gameUser.getUserId());
        initGame.setNames(names);
        initGame.setBoard(initSquare(this.nogibator, this.papkaPro).getSnap());
        final String initGameJson = objectMapper.writeValueAsString(initGame);
        assertNotNull(initGameJson);
    }

    @Test
    public void snapTest() throws JsonProcessingException {
        final Board square = initSquare(this.nogibator, this.papkaPro);
        final String squareJson = objectMapper.writeValueAsString(square.getSnap());
        assertNotNull(squareJson);
    }

    private Board initSquare(GameUser user1, GameUser user2) {
        final GameSession session = mock(GameSession.class);
        when(session.getFirst()).thenReturn(user1);
        when(session.getSecond()).thenReturn(user2);
        return new Board(session, mock(Shuffler.class));
    }


}
