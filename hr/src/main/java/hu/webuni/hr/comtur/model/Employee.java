package hu.webuni.hr.comtur.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import hu.webuni.hr.comtur.dto.IDtoKey;

@Entity
public class Employee implements IDtoKey, Comparable<Employee> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable = false)
	private String name;
	
	// private String position;
	
	@ManyToOne
	@JoinColumn(name = "position_id")
	private Position position;
	
	@Column(nullable = false)
	private int salary;
	
	@Column(nullable = false)
	private LocalDateTime startDate;
	
	@ManyToOne // (cascade = {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "company_id")
	private Company company;
	
	public Employee() {}
	
	public Employee(String name, Position position, int salary, LocalDateTime startDate) {
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.startDate = startDate;
	}
	
	public Employee(long id, String name, Position position, int salary, LocalDateTime startDate) {
		this.id = id;
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.startDate = startDate;
	}
	
	public Employee(String name, Position position, int salary, LocalDateTime startDate, Company company) {
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.startDate = startDate;
		this.company = company;
	}
	
	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}
	
	public int getSalary() {
		return salary;
	}
	
	public void setSalary(int salary) {
		this.salary = salary;
	}
	
	public LocalDateTime getStartDate() {
		return startDate;
	}
	
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public int compareTo(Employee o) {
		return (int)(id - o.getId());
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
		Employee other = (Employee) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", position=" + position + ", salary=" + salary
				+ ", startDate=" + startDate + "]";
	}
}
