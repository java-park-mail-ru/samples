package ru.mail.park.jcstress;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SettableFuture6<V> implements Future<V> {
	private static final Object NOT_SET = new Object();
	private final AtomicReference<V> slot = new AtomicReference<>((V) NOT_SET);

	public V get() throws InterruptedException, ExecutionException {
		if (slot.get() == NOT_SET) {
			synchronized (this) {
				while (slot.get() == NOT_SET) {
					wait();
				}
			}
		}
		return slot.get();
	}

	public boolean set(V value) {
		if (slot.get() == NOT_SET) {
			if (slot.compareAndSet((V) NOT_SET, value)) {
				synchronized (this) {
					notifyAll();
				}
				return true;
			}
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
