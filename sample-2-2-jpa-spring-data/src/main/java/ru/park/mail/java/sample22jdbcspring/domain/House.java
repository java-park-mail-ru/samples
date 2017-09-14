package ru.park.mail.java.sample22jdbcspring.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "house")
public class House {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String words;
	private String sigil;
	@ManyToOne
	private House allegiance;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSigil() {
		return sigil;
	}

	public void setSigil(String sigil) {
		this.sigil = sigil;
	}

	public House getAllegiance() {
		return allegiance;
	}

	public void setAllegiance(House allegiance) {
		this.allegiance = allegiance;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

}
