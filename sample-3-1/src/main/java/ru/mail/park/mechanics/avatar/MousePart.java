package ru.mail.park.mechanics.avatar;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.base.Coords;
import ru.mail.park.mechanics.game.GamePart;
import ru.mail.park.mechanics.game.Snap;

public class MousePart implements GamePart {
    @NotNull
    private Coords mouse;


    public MousePart() {
        this.mouse = new Coords(0.0f, 0.0f);
    }

    @NotNull
    public Coords getMouse() {
        return mouse;
    }

    public void setMouse(@NotNull Coords mouse) {
        this.mouse = mouse;
    }

    @Override
    public MouseSnap takeSnap() {
        return new MouseSnap(this);
    }

    public static final class MouseSnap implements Snap<MousePart> {

        private final Coords mouse;

        public MouseSnap(MousePart mouse) {
            this.mouse = mouse.getMouse();
        }

        public Coords getMouse() {
            return mouse;
        }
    }
}
