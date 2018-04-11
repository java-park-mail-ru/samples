package ru.mail.park.mechanics.messages.inbox;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.base.Coords;
import ru.mail.park.websocket.Message;

/**
 * Created by Solovyev on 03/11/2016.
 */
@SuppressWarnings({"NullableProblems"})
public class ClientSnap extends Message {

    @NotNull
    private Coords mouse;
    private boolean isFiring;
    private long frameTime;

    @NotNull
    public Coords getMouse() {
        return mouse;
    }

    @JsonProperty("isFiring")
    public boolean isFiring() {
        return isFiring;
    }

    public long getFrameTime() {
        return frameTime;
    }

    public void setMouse(@NotNull Coords mouse) {
        this.mouse = mouse;
    }

    public void setFiring(boolean firing) {
        isFiring = firing;
    }

    public void setFrameTime(long frameTime) {
        this.frameTime = frameTime;
    }
}
