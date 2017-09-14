package ru.mail.park.java.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@ManagedResource
@Component
public class MBeanService {
	private final AtomicInteger counter = new AtomicInteger();

	@ManagedAttribute
	public int getCount() {
		return counter.get();
	}

	@ManagedOperation
	public void incrementCount() {
		counter.incrementAndGet();
	}

	@ManagedOperation
	public void incrementCount(int delta) {
		counter.addAndGet(delta);
	}
}
