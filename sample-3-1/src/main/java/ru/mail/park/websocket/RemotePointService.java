package ru.mail.park.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RemotePointService {
    private final Map<Id<UserProfile>, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public RemotePointService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void registerUser(@NotNull Id<UserProfile> userId, @NotNull WebSocketSession webSocketSession) {
        sessions.put(userId, webSocketSession);
    }

    public boolean isConnected(@NotNull Id<UserProfile> userId) {
        return sessions.containsKey(userId) && sessions.get(userId).isOpen();
    }

    public void removeUser(@NotNull Id<UserProfile> userId) {
        sessions.remove(userId);
    }

    public void cutDownConnection(@NotNull Id<UserProfile> userId, @NotNull CloseStatus closeStatus) {
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                webSocketSession.close(closeStatus);
            } catch (IOException ignore) {
            }
        }
    }

    public void sendMessageToUser(@NotNull Id<UserProfile> userId, @NotNull Message message) throws IOException {
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (webSocketSession == null) {
            throw new IOException("no game websocket for user " + userId);
        }
        if (!webSocketSession.isOpen()) {
            throw new IOException("session is closed or not exsists");
        }
        //noinspection OverlyBroadCatchBlock
        try {
            //noinspection ConstantConditions
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            throw new IOException("Unnable to send message", e);
        }
    }
}
