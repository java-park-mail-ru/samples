package ru.mail.park.mechanics.game;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.Config;
import ru.mail.park.mechanics.services.MechanicsTimeService;

public class MechanicPart implements GamePart {
    private int score;
    private boolean isFiring;
    private long lastTimeFired;

    @NotNull
    private final MechanicsTimeService timeService;


    public MechanicPart(@NotNull MechanicsTimeService timeService) {
        this.timeService = timeService;
        score = 0;
        lastTimeFired = -Config.FIRING_COOLDOWN;
        isFiring = false;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        this.score++;
    }

    public boolean tryFire() {
        if (isFiring) {
            return false;
        }
        final long now = timeService.time();
        if (lastTimeFired + Config.FIRING_COOLDOWN <= now) {
            lastTimeFired = now;
            isFiring = true;
            return true;
        }
        return false;
    }

    public void setFiring(boolean firing) {
        isFiring = firing;
    }

    @Override
    public MechanicPartSnap takeSnap() {
        return new MechanicPartSnap(this);
    }

    public static final class MechanicPartSnap implements Snap<MechanicPart> {

        private final int score;
        private final boolean isFiring;

        public MechanicPartSnap(MechanicPart mechanicPart) {
            this.score = mechanicPart.score;
            this.isFiring = mechanicPart.isFiring;

        }

        public int getScore() {
            return score;
        }

        public boolean isFiring() {
            return isFiring;
        }
    }
}
