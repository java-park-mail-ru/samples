package ru.mail.park.websocket;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.mail.park.mechanics.base.Coords;
import ru.mail.park.mechanics.game.Board;
import ru.mail.park.mechanics.messages.inbox.ClientSnap;
import ru.mail.park.mechanics.messages.inbox.JoinGame;
import ru.mail.park.mechanics.messages.outbox.FinishGame;
import ru.mail.park.mechanics.messages.outbox.InitGame;
import ru.mail.park.mechanics.messages.outbox.ServerSnap;
import ru.mail.park.model.Id;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MessageSerializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @MethodSource("twoWaySerializableProvider")
    void test2WaySerializable(Message message) throws IOException {
        final String requestJson = objectMapper.writeValueAsString(message);
        final Message fromJson = objectMapper.readValue(requestJson, Message.class);
        assertEquals(message.getClass(), fromJson.getClass());
    }

    @ParameterizedTest
    @MethodSource("oneWaySerializableProvider")
    void test1WaySerializable(Message message) throws IOException {
        final String messageJson = objectMapper.writeValueAsString(message);
        assertNotNull(messageJson);
    }

    private static Stream<Message> twoWaySerializableProvider() {
        final JoinGame.Request joinRequest = new JoinGame.Request();
        final ClientSnap clientSnap = new ClientSnap();
        clientSnap.setMouse(Coords.of(0, 0));
        final FinishGame finishGame = new FinishGame(FinishGame.Overcome.DRAW);

        return Stream.of(joinRequest,
                         clientSnap,
                         finishGame);

    }

    private static Stream<Message> oneWaySerializableProvider() {
        final InitGame.Request initGame = new InitGame.Request();
        initGame.setBoard(new Board.BoardSnap(List.of(), Id.of(0L), List.of()));
        initGame.setColors(Map.of());
        initGame.setEnemy(Id.of(0));
        initGame.setSelf(Id.of(1));
        initGame.setNames(Map.of());
        initGame.setPlayers(Map.of());
        final ServerSnap serverSnap = new ServerSnap();
        serverSnap.setBoard(new Board.BoardSnap(List.of(), Id.of(0L), List.of()));
        serverSnap.setPlayers(List.of());

        return Stream.of(initGame,
                         serverSnap);
    }
}
