package ru.park.mail.java.sample22jdbcspring;

import java.util.List;

import ru.park.mail.java.sample22jdbcspring.domain.House;
import ru.park.mail.java.sample22jdbcspring.domain.Person;

public interface SampleDao {
	House createHouse(String name, String words, String sigil, Long allegianceId) throws DuplicateHouseException;

	Person createPerson(String name, Long houseId, Long fartherId, Long motherId);

	House getHouse(long id);
	
	Person getPerson(long id);

	List<Person> getAllPeople();

	List<Person> getPeople(long houseId);

	class DuplicateHouseException extends RuntimeException {
		public DuplicateHouseException(String name, Throwable cause) {
			super("House with name " + name + " already exists", cause);
		}
	}

}
