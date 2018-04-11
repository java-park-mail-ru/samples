package ru.mail.park.mechanics;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.messages.inbox.ClientSnap;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;

public interface GameMechanics {

    void addClientSnapshot(@NotNull Id<UserProfile> userId, @NotNull ClientSnap clientSnap);

    void addUser(@NotNull Id<UserProfile> user);

    void gmStep(long frameTime);

    void reset();
}
