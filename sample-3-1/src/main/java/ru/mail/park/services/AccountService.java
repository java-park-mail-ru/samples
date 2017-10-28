package ru.mail.park.services;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;

public interface AccountService {
    UserProfile addUser(String login);

    UserProfile getUserByName(String login);

    UserProfile getUserById(@NotNull Id<UserProfile> id);
}
