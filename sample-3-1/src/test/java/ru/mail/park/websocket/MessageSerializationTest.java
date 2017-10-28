package ru.mail.park.websocket;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.mail.park.mechanics.requests.JoinGame;

@SuppressWarnings("OverlyBroadThrowsClause")
public class MessageSerializationTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void joinGameTest() throws IOException {
        final JoinGame.Request request = new JoinGame.Request();
        final String requestJson = objectMapper.writeValueAsString(request);
        final Message fromJson = objectMapper.readValue(requestJson, Message.class);
        Assert.assertTrue(fromJson instanceof JoinGame.Request);
    }

}
