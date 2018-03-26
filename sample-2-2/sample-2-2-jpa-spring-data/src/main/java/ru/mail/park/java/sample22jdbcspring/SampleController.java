package ru.mail.park.java.sample22jdbcspring;

import org.springframework.web.bind.annotation.PutMapping;

import ru.mail.park.java.sample22jdbcspring.domain.House;
import ru.mail.park.java.sample22jdbcspring.domain.Person;

public abstract class SampleController {

	private final SampleDao dao;

	SampleController(SampleDao dao) {
		this.dao = dao;
	}

	@PutMapping("person")
	public Person createPerson(String name, Long house, Long farther, Long mother) {
		return dao.createPerson(name, house, farther, mother);
	}

	@PutMapping("house")
	public House createHouse(String name, String words, String sigil, Long allegiance) {
		return dao.createHouse(name, words, sigil, allegiance);
	}
}
