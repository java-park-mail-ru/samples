package ru.mail.park.jcstress;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.ZZZZ_Result;

@JCStressTest
@Outcome(id = "true, false, true, false", expect = Expect.ACCEPTABLE, desc = "Successfully set foo")
@Outcome(id = "false, true, false, true", expect = Expect.ACCEPTABLE, desc = "Successfully set bar")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
public class SettableFuture6Test {

	private final SettableFuture6<String> future = new SettableFuture6<>();

	@Actor
	public void setFoo(ZZZZ_Result r) {
		r.r1 = future.set("foo");
	}

	@Actor
	public void setBar(ZZZZ_Result r) {
		r.r2 = future.set("bar");
	}

	@Actor
	public void getFoo(ZZZZ_Result r) {
		try {
			r.r3 = "foo".equals(future.get());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Actor
	public void getBar(ZZZZ_Result r) {
		try {
			r.r4 = "bar".equals(future.get());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}


