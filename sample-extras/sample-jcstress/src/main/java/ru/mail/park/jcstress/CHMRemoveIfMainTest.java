package ru.mail.park.jcstress;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

//Copy&Paste from http://bugs.java.com/bugdatabase/view_bug.do?bug_id=8078726
public class CHMRemoveIfMainTest {

	private static final Random RANDOM = new Random();

	private static final ConcurrentHashMap<Integer, Boolean> MAP = new ConcurrentHashMap<Integer, Boolean>();

	private static final Integer KEY = 1;

	private static final Thread DELETING_THREAD = new Thread() {

		@Override
		public void run() {
			while (true) {
				MAP.entrySet().removeIf(entry -> entry.getValue() == false);
			}
		}

	};

	private static final Thread ADDING_THREAD = new Thread() {

		@Override
		public void run() {
			while (true) {
				boolean val = RANDOM.nextBoolean();

				MAP.put(KEY, val);
				if (val == true && !MAP.containsKey(KEY)) {
					throw new RuntimeException("TRUE value was removed");
				}

			}
		}

	};

	public static void main(String[] args) throws InterruptedException {
		DELETING_THREAD.setDaemon(true);
		ADDING_THREAD.start();
		DELETING_THREAD.start();
		ADDING_THREAD.join();
	}
}