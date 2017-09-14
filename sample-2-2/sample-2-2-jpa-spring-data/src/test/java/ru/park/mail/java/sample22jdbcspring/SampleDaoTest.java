package ru.park.mail.java.sample22jdbcspring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.persistence.PersistenceException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.park.mail.java.sample22jdbcspring.SampleDao.DuplicateHouseException;
import ru.park.mail.java.sample22jdbcspring.domain.House;
import ru.park.mail.java.sample22jdbcspring.domain.Person;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public abstract class SampleDaoTest {

	
	protected abstract SampleDao getDao();
	
	@Rule
	public ExpectedException expected = ExpectedException.none();

	@Test
	public void testCreateHouse() {
		House house = getDao().createHouse("testname", "testwords", "testsigil", null);
		assertNotNull(house.getId());
		assertEquals("testname", house.getName());
		assertEquals("testwords", house.getWords());
		assertEquals("testsigil", house.getSigil());
		assertNull(house.getAllegiance());
	}

	@Test
	public void testCreatePerson() {
		Person person = getDao().createPerson("testname", null, null, null);
		assertNotNull(person.getId());
		assertEquals("testname", person.getName());
		assertNull(person.getHouse());
		assertNull(person.getFarther());
		assertNull(person.getMother());
		
		assertEquals(person, getDao().getPerson(person.getId()));
	}

	@Test
	public void testSnow() {
		House starks = getDao().createHouse("Starks", "Winter is coming", "Direwolf", null);
		Person ned = getDao().createPerson("Eddard", starks.getId(), null, null);
		Person john = getDao().createPerson("John", null, ned.getId(), null);
		
		assertEquals(ned, getDao().getPerson(ned.getId()));
		assertEquals(john, getDao().getPerson(john.getId()));
	}

	@Test
	public void testCreateDuplicateHouse() {
		getDao().createHouse("testname", "testwords", "testsigil", null);
		expected.expect(DuplicateHouseException.class);
		expected.expectMessage("House with name testname already exists");
		getDao().createHouse("testname", "testwords", "testsigil", null);
	}
}
