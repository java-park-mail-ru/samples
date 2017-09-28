package ru.mail.park.java.domain;

import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Before;

/**
 * Created by isopov on 29.09.16.
 */
public class UserTest {

	private User foo, bar;

	@Before
	public void setup() {
		foo = new User("foo");
		bar = new User("bar");
	}

	@Test
	public void testIncreasePower() {
		foo.increasePower();
		assertEquals(1, foo.getPower());
	}

	@Test
	public void testFight() {
		testIncreasePower();
		assertEquals(0, foo.getWins());
		assertEquals(0, bar.getWins());

		foo.fight(bar);

		assertEquals(1, foo.getWins());
		assertEquals(0, bar.getWins());
	}

}