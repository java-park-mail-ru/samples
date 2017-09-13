package ru.park.mail.java.sample22jdbcspring.domain;

public class PersonRelation {
	private final long subjectId;
	private final long objectId;
	private final Relation relation;

	public PersonRelation(long subjectId, long objectId, Relation relation) {
		this.subjectId = subjectId;
		this.objectId = objectId;
		this.relation = relation;
	}

	public long getSubjectId() {
		return subjectId;
	}

	public long getObjectId() {
		return objectId;
	}

	public Relation getRelation() {
		return relation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (objectId ^ (objectId >>> 32));
		result = prime * result + ((relation == null) ? 0 : relation.hashCode());
		result = prime * result + (int) (subjectId ^ (subjectId >>> 32));
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
		PersonRelation other = (PersonRelation) obj;
		if (objectId != other.objectId)
			return false;
		if (relation != other.relation)
			return false;
		if (subjectId != other.subjectId)
			return false;
		return true;
	}
}
