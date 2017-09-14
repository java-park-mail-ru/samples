package ru.park.mail.java.sample22jdbcplain.domain;

public class House {
	private final long id;
	private final String name;
	private final String words;
	private final String sigil;
	private final Long allegianceId;

	public House(long id, String name, String words, String sigil, Long allegianceId) {
		this.id = id;
		this.name = name;
		this.words = words;
		this.sigil = sigil;
		this.allegianceId = allegianceId;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getWords() {
		return words;
	}

	public String getSigil() {
		return sigil;
	}

	public Long getAllegianceId() {
		return allegianceId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((allegianceId == null) ? 0 : allegianceId.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((sigil == null) ? 0 : sigil.hashCode());
		result = prime * result + ((words == null) ? 0 : words.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		House other = (House) obj;
		if (allegianceId == null) {
			if (other.allegianceId != null)
				return false;
		} else if (!allegianceId.equals(other.allegianceId))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sigil == null) {
			if (other.sigil != null)
				return false;
		} else if (!sigil.equals(other.sigil))
			return false;
		if (words == null) {
			if (other.words != null)
				return false;
		} else if (!words.equals(other.words))
			return false;
		return true;
	}

}
