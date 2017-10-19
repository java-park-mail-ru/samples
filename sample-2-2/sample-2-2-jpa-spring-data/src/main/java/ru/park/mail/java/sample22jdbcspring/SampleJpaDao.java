package ru.park.mail.java.sample22jdbcspring;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.park.mail.java.sample22jdbcspring.domain.House;
import ru.park.mail.java.sample22jdbcspring.domain.Person;

@Service
@Transactional
public class SampleJpaDao implements SampleDao {

	private final EntityManager em;

	public SampleJpaDao(EntityManager em) {
		this.em = em;
	}

	@Override
	public House createHouse(String name, String words, String sigil, Long allegianceId) {
		House house = new House();
		house.setName(name);
		house.setWords(words);
		house.setSigil(sigil);
		if (allegianceId != null) {
			house.setAllegiance(em.find(House.class, allegianceId));
		}
		try {
			em.persist(house);
		} catch (PersistenceException ex) {
			if (ex.getCause() instanceof ConstraintViolationException) {
				throw new DuplicateHouseException(name, ex);
			} else {
				throw ex;
			}
		}
		return house;
	}

	@Override
	public Person createPerson(String name, Long houseId, Long fartherId, Long motherId) {

		Person person = new Person();
		person.setName(name);
		if (houseId != null) {
			person.setHouseId(em.find(House.class, houseId));
		}
		if (fartherId != null) {
			person.setFarther(em.find(Person.class, fartherId));
		}

		if (motherId != null) {
			person.setMother(em.find(Person.class, motherId));
		}
		em.persist(person);
		return person;
	}

	@Override
	public House getHouse(long id) {
		return em.find(House.class, id);
	}

	@Override
	public Person getPerson(long id) {
		return em.find(Person.class, id);
	}

	@Override
	public List<Person> getAllPeople() {
		return em.createQuery("select * from Person", Person.class).getResultList();
	}

	@Override
	public List<Person> getPeople(long houseId) {
		return em.createQuery("select * from Person where house.id=:houseId", Person.class)
				.setParameter("houseId", houseId).getResultList();
	}
}
