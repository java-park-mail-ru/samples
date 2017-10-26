package ru.mail.park.mechanics.game;

/**
 * Created by Solovyev on 04/11/2016.
 */
public interface GamePart {

    default boolean shouldBeSnaped() {
        return true;
    }

    Snap<? extends GamePart> takeSnap();
}
