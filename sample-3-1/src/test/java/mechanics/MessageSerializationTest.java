package mechanics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.mail.park.mechanics.requests.JoinGame;
import ru.mail.park.websocket.Message;

import java.io.IOException;

@SuppressWarnings("OverlyBroadThrowsClause")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class MessageSerializationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void joinGameTest() throws IOException {
        final JoinGame.Request request = new JoinGame.Request();
        final String requestJson = objectMapper.writeValueAsString(request);
        final Message fromJson = objectMapper.readValue(requestJson, Message.class);
        Assert.assertTrue(fromJson instanceof JoinGame.Request);
    }

}
