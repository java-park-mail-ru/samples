package ru.mail.park.java.sample22jdbcspring;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mail.park.java.sample22jdbcplain.House;
import ru.mail.park.java.sample22jdbcplain.Person;
import ru.mail.park.java.sample22jdbcplain.SampleDao;

class SampleDaoTest {

	private static HikariDataSource ds;
	private static SampleDao dao;

	@BeforeAll
	static void setUpDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:postgresql://localhost/sample22plain");
		config.setUsername("sample22plain");
		config.setPassword("sample22plain");
		config.setMaximumPoolSize(1);

		ds = new HikariDataSource(config);
		dao = new SampleDao(ds);
	}

	@AfterAll
	static void tearDownDataSource() {
		ds.close();
	}

	@BeforeEach
	void setup() {
		clearDb();
	}

	@AfterEach
	void tearDown() {
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
	void testCreateHouse() {
		House house = dao.createHouse("testname", "testwords", "testsigil", null);
		assertTrue(house.getId() > 0);
		assertEquals("testname", house.getName());
		assertEquals("testwords", house.getWords());
		assertEquals("testsigil", house.getSigil());
		assertNull(house.getAllegianceId());
	}

	@Test
	void testCreatePerson() {
		Person person = dao.createPerson("testname", null, null, null);
		assertTrue(person.getId() > 0);
		assertEquals("testname", person.getName());
		assertNull(person.getHouseId());
		assertNull(person.getFartherId());
		assertNull(person.getMotherId());

		assertEquals(person, dao.getPerson(person.getId()));
	}

	@Test
	void testSnow() {
		House starks = dao.createHouse("Starks", "Winter is coming", "Direwolf", null);
		Person ned = dao.createPerson("Eddard", starks.getId(), null, null);
		Person john = dao.createPerson("John", null, ned.getId(), null);

		assertEquals(ned, dao.getPerson(ned.getId()));
		assertEquals(john, dao.getPerson(john.getId()));
		assertEquals(2, dao.getAllPeople().size());
		assertEquals(1, dao.getPeople(starks.getId()).size());
	}
}
