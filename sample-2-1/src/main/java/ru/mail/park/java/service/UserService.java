package ru.mail.park.java.service;

import org.springframework.stereotype.Service;
import ru.mail.park.java.domain.User;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public User getUser(String userName) {
        return users.get(userName);
    }

    public User ensureUserExists(String userName) {
        return users.computeIfAbsent(userName, User::new);
    }
}
