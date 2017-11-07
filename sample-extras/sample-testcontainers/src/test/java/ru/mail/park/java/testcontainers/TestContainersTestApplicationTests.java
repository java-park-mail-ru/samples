package ru.mail.park.java.testcontainers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestContainersTestApplicationTests {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void testSimple() {
		assertEquals(0, (int) jdbcTemplate.queryForObject("select count(*) from foo", Integer.class));
	}

}
