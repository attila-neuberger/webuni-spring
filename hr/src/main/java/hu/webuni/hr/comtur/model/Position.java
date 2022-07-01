package hu.webuni.hr.comtur.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Position extends Id {

	@Column(nullable = false)
	private String name;
	
	@Column(nullable = true)
	private Education minEducation;
	
	@OneToMany(mappedBy = "position")
	private List<Employee> employees;
	
	public Position() {
		super();
	}

	public Position(String name, Education minEducation) {
		super();
		this.name = name;
		this.minEducation = minEducation;
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
	public String toString() {
		return "Position [id=" + getId() + ", name=" + name + ", minEducation=" + minEducation + "]";
	}
}
