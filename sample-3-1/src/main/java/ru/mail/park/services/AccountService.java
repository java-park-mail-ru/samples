package ru.mail.park.services;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;

/**
 * Created by Solovyev on 30/10/2016.
 */
public interface AccountService {
    UserProfile addUser(String login, String password, String email);

    UserProfile getUserByName(String login);

    UserProfile getUserById(@NotNull Id<UserProfile> id);

    boolean checkAuth(@NotNull Id<UserProfile> userId, @NotNull String password);
}
