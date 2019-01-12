package ru.mail.park.java.sample22jdbcplain;

public class Person {
	private final long id;
	private final String name;
	private final Long houseId;
	private final Long fartherId;
	private final Long motherId;

	public Person(long id, String name, Long houseId, Long fartherId, Long motherId) {
		this.id = id;
		this.name = name;
		this.houseId = houseId;
		this.fartherId = fartherId;
		this.motherId = motherId;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getHouseId() {
		return houseId;
	}

	public Long getFartherId() {
		return fartherId;
	}

	public Long getMotherId() {
		return motherId;
	}

	@Override
	public int hashCode() {
		final var prime = 31;
		int result = 1;
		result = prime * result + ((fartherId == null) ? 0 : fartherId.hashCode());
		result = prime * result + ((houseId == null) ? 0 : houseId.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((motherId == null) ? 0 : motherId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		var other = (Person) obj;
		if (fartherId == null) {
			if (other.fartherId != null)
				return false;
		} else if (!fartherId.equals(other.fartherId))
			return false;
		if (houseId == null) {
			if (other.houseId != null)
				return false;
		} else if (!houseId.equals(other.houseId))
			return false;
		if (id != other.id)
			return false;
		if (motherId == null) {
			if (other.motherId != null)
				return false;
		} else if (!motherId.equals(other.motherId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
