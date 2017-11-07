package ru.mail.park.java.sample22jdbcspring;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.mail.park.java.sample22jdbcspring.domain.House;
import ru.mail.park.java.sample22jdbcspring.domain.Person;
import ru.mail.park.java.sample22jdbcspring.repositories.HouseRepository;
import ru.mail.park.java.sample22jdbcspring.repositories.PersonRepository;

@Service
@Transactional
public class SampleSpringDataDao implements SampleDao {

	private final HouseRepository houseRepository;
	private final PersonRepository personRepository;

	public SampleSpringDataDao(HouseRepository houseRepository, PersonRepository personRepository) {
		this.houseRepository = houseRepository;
		this.personRepository = personRepository;
	}

	@Override
	public House createHouse(String name, String words, String sigil, Long allegianceId) {
		House house = new House();
		house.setName(name);
		house.setWords(words);
		house.setSigil(sigil);
		if (allegianceId != null) {
			house.setAllegiance(houseRepository.getOne(allegianceId));
		}
		try {
			return houseRepository.save(house);
		} catch (DataIntegrityViolationException ex) {
			throw new DuplicateHouseException(name, ex);
		}
	}

	@Override
	public Person createPerson(String name, Long houseId, Long fartherId, Long motherId) {

		Person person = new Person();
		person.setName(name);
		if (houseId != null) {
			person.setHouseId(houseRepository.getOne(houseId));
		}
		if (fartherId != null) {
			person.setFarther(personRepository.getOne(fartherId));
		}

		if (motherId != null) {
			person.setMother(personRepository.getOne(motherId));
		}
		return personRepository.save(person);
	}

	
	
	@Override
	public House getHouse(long id) {
		return houseRepository.getOne(id);
	}

	@Override
	public Person getPerson(long id) {
		return personRepository.getOne(id);
	}

	@Override
	public List<Person> getAllPeople() {
		return personRepository.findAll();
	}

	@Override
	public List<Person> getPeople(long houseId) {
		return personRepository.findAllByHouse(houseId);
	}
}
