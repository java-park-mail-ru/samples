package ru.park.mail.java.sample22jdbcspring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.park.mail.java.sample22jdbcspring.domain.House;
import ru.park.mail.java.sample22jdbcspring.domain.Person;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SampleDaoTest {

	@Autowired
	private SampleDao dao;
	
	@Rule
	public ExpectedException expected = ExpectedException.none();

	@Test
	public void testCreateHouse() {
		House house = dao.createHouse("testname", "testwords", "testsigil", null);
		assertTrue(house.getId() > 0);
		assertEquals("testname", house.getName());
		assertEquals("testwords", house.getWords());
		assertEquals("testsigil", house.getSigil());
		assertNull(house.getAllegianceId());
	}

	@Test
	public void testCreatePerson() {
		Person person = dao.createPerson("testname", null, null, null);
		assertTrue(person.getId() > 0);
		assertEquals("testname", person.getName());
		assertNull(person.getHouseId());
		assertNull(person.getFartherId());
		assertNull(person.getMotherId());
		
		assertEquals(person, dao.getPerson(person.getId()));
	}

	@Test
	public void testSnow() {
		House starks = dao.createHouse("Starks", "Winter is coming", "Direwolf", null);
		Person ned = dao.createPerson("Eddard", starks.getId(), null, null);
		Person john = dao.createPerson("John", null, ned.getId(), null);
		
		assertEquals(ned, dao.getPerson(ned.getId()));
		assertEquals(john, dao.getPerson(john.getId()));
	}

	@Test
	public void testCreateDuplicateHouse() {
		dao.createHouseNamed("testname", "testwords", "testsigil", null);
		expected.expect(DuplicateKeyException.class);
		dao.createHouseNamed("testname", "testwords", "testsigil", null);
	}
	
	@Test
	public void testCreateHouseNamed() {
		House house = dao.createHouseNamed("testname", "testwords", "testsigil", null);
		assertTrue(house.getId() > 0);
		assertEquals("testname", house.getName());
		assertEquals("testwords", house.getWords());
		assertEquals("testsigil", house.getSigil());
		assertNull(house.getAllegianceId());
	}

}
