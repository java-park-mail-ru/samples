package ru.park.mail.java.sample22jdbcspring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.park.mail.java.sample22jdbcspring.domain.House;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SampleDaoTest {

	@Autowired
	private SampleDao dao;

	@Test
	public void testCreateHouse() {
		House house = dao.createHouse("testname", "testwords", "testsigil", null);
		assertTrue(house.getId() > 0);
		assertEquals("testname", house.getName());
		assertEquals("testwords", house.getWords());
		assertEquals("testsigil", house.getSigil());
		assertNull(house.getAllegianceId());

	}

}
