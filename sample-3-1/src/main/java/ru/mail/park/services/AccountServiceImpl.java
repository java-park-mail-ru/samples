package ru.mail.park.services;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountServiceImpl implements AccountService {

    @NotNull
    private final Map<String, UserProfile> userNameToUser = new ConcurrentHashMap<>();
    private final Map<Id<UserProfile>, UserProfile> userIdToUser = new ConcurrentHashMap<>();

    @Override
    public UserProfile addUser(String login, String password, String email) {
        final UserProfile userProfile = new UserProfile(login, BCrypt.hashpw(password, BCrypt.gensalt()));
        userNameToUser.put(login, userProfile);
        userIdToUser.put(userProfile.getId(), userProfile);
        return userProfile;
    }
    @Override
    public UserProfile getUserByName(String login) {
        return userNameToUser.get(login);
    }

    @Override
    public UserProfile getUserById(@NotNull Id<UserProfile> id) {
        return  userIdToUser.get(id);
    }

    @Override
    public boolean checkAuth(@NotNull Id<UserProfile> userId, @NotNull String password) {
        final UserProfile userProfile = userIdToUser.get(userId);
        return userProfile != null && BCrypt.checkpw(password, userProfile.getPasswordHash());
    }
}
