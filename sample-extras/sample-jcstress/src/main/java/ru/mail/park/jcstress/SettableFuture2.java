package ru.mail.park.jcstress;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SettableFuture2<V> implements Future<V> {
	private V slot;

	public V get() throws InterruptedException, ExecutionException {
		while (slot == null) {
			synchronized (this) {
				wait();
			}
		}
		return slot;
	}

	public void set(V value) {
		slot = value;
		synchronized (this) {
			notifyAll();
		}
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return get();
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		throw new IllegalStateException();
	}

	@Override
	public boolean isCancelled() {
		throw new IllegalStateException();
	}

	@Override
	public boolean isDone() {
		throw new IllegalStateException();
	}
}
