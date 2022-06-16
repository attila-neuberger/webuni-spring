package hu.webuni.hr.comtur.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Position {

	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = true)
	private Education minEducation;
	
	@OneToMany(mappedBy = "position")
	private List<Employee> employees;
	
	public Position() {}

	public Position(String name, Education minEducation) {
		this.name = name;
		this.minEducation = minEducation;
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

	public Education getMinEducation() {
		return minEducation;
	}

	public void setMinEducation(Education minEducation) {
		this.minEducation = minEducation;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
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
		Position other = (Position) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Position [id=" + id + ", name=" + name + ", minEducation=" + minEducation + "]";
	}
}
