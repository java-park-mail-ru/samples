package ru.mail.park.mechanics.internal;

import org.springframework.stereotype.Service;

@Service
public class MechanicsTimeService {
    private long millis = 0;


    public void tick(long delta) {
        millis += delta;
    }

    public long time() {
        return millis;
    }
}
