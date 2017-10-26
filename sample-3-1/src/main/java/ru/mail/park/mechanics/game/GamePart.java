package ru.mail.park.mechanics.game;

public interface GamePart {

    default boolean shouldBeSnaped() {
        return true;
    }

    Snap<? extends GamePart> takeSnap();
}
