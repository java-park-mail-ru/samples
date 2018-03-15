package ru.mail.park.java.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private final String name;
    private long power;
    private int wins;

    @JsonCreator
    public User(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getPower() {
        return power;
    }

    public int getWins() {
        return wins;
    }

    public synchronized User increasePower() {
        power++;
        return this;
    }

    public synchronized User fight(User otherUser) {
        synchronized (otherUser) {
            if (power > otherUser.power) {
                wins++;
                power -= otherUser.power;
                otherUser.power = 0;
            } else if (power < otherUser.power) {
                otherUser.wins++;
                otherUser.power -= power;
                power = 0;
            }
            return this;
        }
    }
}
