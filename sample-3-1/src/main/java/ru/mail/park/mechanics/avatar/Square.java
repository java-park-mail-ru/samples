package ru.mail.park.mechanics.avatar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mail.park.mechanics.game.GameObject;
import ru.mail.park.mechanics.game.Snap;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;

public class Square extends GameObject {
    @Nullable
    private Id<UserProfile> occupant;

    @Nullable
    public Id<UserProfile> getOccupant() {
        return occupant;
    }

    public void setOccupant(@Nullable Id<UserProfile> occupant) {
        this.occupant = occupant;
    }

    @Override
    @NotNull
    public SquareSnap getSnap() {
        return new SquareSnap(this);
    }

    @SuppressWarnings("unused")
    public static final class SquareSnap implements Snap<Square> {

        @Nullable
        private final Id<UserProfile> occupant;

        public SquareSnap(@NotNull Square square) {
            this.occupant = square.occupant;
        }

        @Nullable
        public Id<UserProfile> getOccupant() {
            return occupant;
        }
    }

    @Override
    public String toString() {
        return "Square{" +
                "occupant=" + occupant +
                '}';
    }
}
