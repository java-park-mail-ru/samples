package ru.mail.park.services;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountServiceImpl implements AccountService {

    @NotNull
    private final Map<String, UserProfile> userNameToUser = new ConcurrentHashMap<>();
    private final Map<Id<UserProfile>, UserProfile> userIdToUser = new ConcurrentHashMap<>();

    @Override
    public UserProfile addUser(String login) {
        final UserProfile userProfile = new UserProfile(login);
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
        return userIdToUser.get(id);
    }


}
