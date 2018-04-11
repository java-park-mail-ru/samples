package ru.mail.park.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.mail.park.mechanics.messages.inbox.ClientSnap;
import ru.mail.park.mechanics.messages.outbox.FinishGame;
import ru.mail.park.mechanics.messages.outbox.ServerSnap;
import ru.mail.park.mechanics.messages.outbox.InitGame;
import ru.mail.park.mechanics.messages.inbox.JoinGame;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @Type(JoinGame.Request.class),
        @Type(InitGame.Request.class),
        @Type(ClientSnap.class),
        @Type(ServerSnap.class),
        @Type(FinishGame.class),
})
public abstract class Message {
}
