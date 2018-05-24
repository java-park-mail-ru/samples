package ru.mail.park.jcstress;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SettableFuture1<V> implements Future<V> {
    private V slot;

	@Override
    public V get() throws InterruptedException, ExecutionException {
        wait();
        return slot;
    }

    public void set(V value) {
        slot = value;
        notifyAll();
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
