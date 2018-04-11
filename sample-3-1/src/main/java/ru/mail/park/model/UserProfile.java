package ru.mail.park.model;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Solovyev on 17/09/16.
 */
public class UserProfile {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    @NotNull
    private final Id<UserProfile> id;
    @NotNull
    private final String login;

    public UserProfile(@NotNull String login) {
        id = Id.of(ID_GENERATOR.getAndIncrement());
        this.login = login;

    }

    @NotNull
    public Id<UserProfile> getId() {
        return id;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return "UserProfile{"
                + "id=" + id
                + ", login='" + login + '\''
                + '}';
    }
}
