package hu.webuni.hr.comtur.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;

import hu.webuni.hr.comtur.dto.Views.VisibleData;

/*public enum CompanyType {
	BT,
	KFT,
	ZRT,
	NYRT
}*/

@Entity
public class CompanyType {
	
	@Id
	@GeneratedValue
	@JsonView(VisibleData.class)
	private long id;
	
	@JsonView(VisibleData.class)
	private String name;
	
	public CompanyType() {
	}

	public CompanyType(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		CompanyType other = (CompanyType) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "CompanyType [id=" + id + ", name=" + name + "]";
	}
}
