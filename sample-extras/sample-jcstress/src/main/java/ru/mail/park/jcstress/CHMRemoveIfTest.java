package ru.mail.park.jcstress;

import java.util.concurrent.ConcurrentHashMap;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.ZZZZ_Result;

@JCStressTest
@Outcome(id = "true, false, true, false", expect = Expect.ACCEPTABLE, desc = "Put T, Put F, Remove F")
@Outcome(id = "true, true, true, true", expect = Expect.ACCEPTABLE, desc = "Put F, Remove F, Put T")
@Outcome(id = "false, true, false, true", expect = Expect.ACCEPTABLE, desc = "Put F, Put T, Remove Nothing")
@Outcome(id = "false, true, true, false", expect = Expect.FORBIDDEN, desc = "Put F, Put T, !!!Remove T!!!")
@Outcome(expect = Expect.FORBIDDEN, desc = "Other cases are forbidden.")
@State
//inspired by http://bugs.java.com/bugdatabase/view_bug.do?bug_id=8078726
public class CHMRemoveIfTest {

	private final ConcurrentHashMap<Integer, Boolean> map = new ConcurrentHashMap<>();

	@Actor
	public void setFoo(ZZZZ_Result r) {
		r.r1 = null == map.put(1, true);
	}

	@Actor
	public void removeIf(ZZZZ_Result r) {
		r.r2 = null == map.put(1, false);
		r.r3 = map.entrySet().removeIf(e -> e.getValue() == false);
	}

	@Arbiter
	public void after(ZZZZ_Result r) {
		r.r4 = map.containsKey(1);
	}
}
