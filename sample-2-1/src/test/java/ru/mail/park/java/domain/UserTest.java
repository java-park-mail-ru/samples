package ru.mail.park.java.domain;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by isopov on 29.09.16.
 */
public class UserTest {

    @Test
    public void testFight() {
        User foo = new User("foo");
        User bar = new User("bar");

        foo.increasePower();
        assertEquals(1, foo.getPower());

        assertEquals(0, foo.getWins());
        assertEquals(0, bar.getWins());

        foo.fight(bar);

        assertEquals(1, foo.getWins());
        assertEquals(0, bar.getWins());
    }
}