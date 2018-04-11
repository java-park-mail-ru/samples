package ru.mail.park.mechanics.messages.outbox;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.mail.park.websocket.Message;

public class FinishGame extends Message {
    private Overcome overcome;

    @JsonCreator
    public FinishGame(@JsonProperty("overcome") Overcome overcome) {
        this.overcome = overcome;
    }

    public Overcome getOvercome() {
        return overcome;
    }

    @SuppressWarnings("FieldNamingConvention")
    public enum Overcome {
        WIN,
        LOSE,
        DRAW
    }
}
