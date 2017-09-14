package ru.mail.park.java.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import ru.mail.park.java.domain.User;

@Service
public class UsersService {
	private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

	public User getUser(String username) {
		return users.get(username);
	}

	public User ensureUserExists(String username) {
		return users.computeIfAbsent(username, User::new);
	}

}
