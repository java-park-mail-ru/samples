package ru.mail.park.jcstress;

public class SettableFuture1Test {
	public static void main(String[] args) throws Exception {
		//Ну это просто вообще не работает
		SettableFuture1<Object> future = new SettableFuture1<>();
		future.set(new Object());
		System.out.println(future.get());
	}
}
