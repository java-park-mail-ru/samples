package ru.mail.park.jcstress;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public class SettableFuture3<V> implements Future<V> {
	private V slot;
	private final AtomicBoolean isSet = new AtomicBoolean();

	@Override
	public V get() throws InterruptedException, ExecutionException {
		synchronized (this) {
			while (!isSet.get()) {
				wait();
			}
		}
		return slot;
	}

	public boolean set(V value) {
		if (isSet.compareAndSet(false, true)) {
			slot = value;
			synchronized (this) {
				notifyAll();
			}
			return true;
		}
		return false;
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
