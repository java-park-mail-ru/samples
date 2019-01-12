package ru.mail.park.jcstress;

public class SettableFuture2Test2 {
	public static void main(String[] args) throws Exception {
		var future = new SettableFuture2<>();
		var value = new Object();
		future.set(value);
		future.set(null);
		System.out.println(future.get() == value);
	}
}
