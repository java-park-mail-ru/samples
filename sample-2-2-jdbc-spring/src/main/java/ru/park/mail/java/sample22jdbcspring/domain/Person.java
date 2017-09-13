package ru.park.mail.java.sample22jdbcspring.domain;

public class Person {
	private final long id;
	private final String name;
	private final long houseId;
	private final long fartherId;
	private final long motherId;

	public Person(long id, String name, long houseId, long fartherId, long motherId) {
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

	public long getHouseId() {
		return houseId;
	}

	public long getFartherId() {
		return fartherId;
	}

	public long getMotherId() {
		return motherId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (fartherId ^ (fartherId >>> 32));
		result = prime * result + (int) (houseId ^ (houseId >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (motherId ^ (motherId >>> 32));
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
		Person other = (Person) obj;
		if (fartherId != other.fartherId)
			return false;
		if (houseId != other.houseId)
			return false;
		if (id != other.id)
			return false;
		if (motherId != other.motherId)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
