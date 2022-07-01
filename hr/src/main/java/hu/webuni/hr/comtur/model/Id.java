package hu.webuni.hr.comtur.model;

import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.MappedSuperclass;

import hu.webuni.hr.comtur.dto.IDtoKey;

@MappedSuperclass
public class Id implements IDtoKey {

	@javax.persistence.Id
	@GeneratedValue
	private long id;

	public Id() {}

	public Id(long id) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Id other = (Id) obj;
		return id == other.id;
	}
}
