package ru.mail.park.mechanics.services;

import org.springframework.stereotype.Service;

@Service
public class MechanicsTimeService {
    private long millis = 0;

    public void reset() {
        millis = 0;
    }

    public void tick(long delta) {
        millis += delta;
    }

    public long time() {
        return millis;
    }
}
