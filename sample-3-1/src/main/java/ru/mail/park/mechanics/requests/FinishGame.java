package ru.mail.park.mechanics.requests;

import ru.mail.park.websocket.Message;

@SuppressWarnings("unused")
public class FinishGame extends Message {
    private Overcome overcome;

    public FinishGame(Overcome overcome) {
        this.overcome = overcome;
    }

    public Overcome getOvercome() {
        return overcome;
    }

    public enum Overcome {
        WIN,
        LOSE,
        DRAW
    }
}
