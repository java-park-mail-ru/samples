package ru.park.mail.java.sample22jdbcspring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import ru.park.mail.java.sample22jdbcplain.SampleDao;
import ru.park.mail.java.sample22jdbcplain.domain.House;
import ru.park.mail.java.sample22jdbcplain.domain.Person;

public class SampleDaoTest {

	private static HikariDataSource ds;
	private static SampleDao dao;

	@BeforeClass
	public static void setUpDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:postgresql://localhost/sample22plain");
		config.setUsername("sample22plain");
		config.setPassword("sample22plain");
		config.setMaximumPoolSize(1);

		ds = new HikariDataSource(config);
		dao = new SampleDao(ds);
	}

	@AfterClass
	public static void tearDownDataSource() {
		ds.close();
	}

	@Before
	public void setup() {
		clearDb();
	}

	@After
	public void tearDown() {
		clearDb();
	}

	private void clearDb() {
		try (Connection con = ds.getConnection(); Statement st = con.createStatement()) {
			st.executeUpdate("truncate house, person");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

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
		assertEquals(2, dao.getAllPeople().size());
		assertEquals(1, dao.getPeople(starks.getId()).size());
	}
}
