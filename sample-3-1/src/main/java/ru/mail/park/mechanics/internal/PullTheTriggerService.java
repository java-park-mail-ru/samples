package ru.mail.park.mechanics.internal;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.mail.park.mechanics.GameSession;
import ru.mail.park.mechanics.avatar.MechanicPart;

@Service
public class PullTheTriggerService {
    public void pullTheTriggers(@NotNull GameSession session) {
        session.getPlayers().forEach(user -> user.claimPart(MechanicPart.class).setFiring(false));
    }
}
