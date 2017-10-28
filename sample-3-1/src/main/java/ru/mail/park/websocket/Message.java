package ru.mail.park.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.mail.park.mechanics.base.ClientSnap;
import ru.mail.park.mechanics.base.ServerSnap;
import ru.mail.park.mechanics.requests.InitGame;
import ru.mail.park.mechanics.requests.JoinGame;

@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @Type(JoinGame.Request.class),
        @Type(InitGame.Request.class),
        @Type(ClientSnap.class),
        @Type(ServerSnap.class),
        })
public abstract class Message {
}
