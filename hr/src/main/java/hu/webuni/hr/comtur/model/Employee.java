package hu.webuni.hr.comtur.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Employee extends Id implements Comparable<Employee> {

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
	
	public Employee() {
		super();
	}
	
	public Employee(String name, Position position, int salary, LocalDateTime startDate) {
		super();
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.startDate = startDate;
	}
	
	public Employee(long id, String name, Position position, int salary, LocalDateTime startDate) {
		super(id);
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.startDate = startDate;
	}
	
	public Employee(String name, Position position, int salary, LocalDateTime startDate, Company company) {
		super();
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.startDate = startDate;
		this.company = company;
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
		return (int)(getId() - o.getId());
	}

	@Override
	public String toString() {
		return "Employee [id=" + getId() + ", name=" + name + ", position=" + position + ", salary=" + salary
				+ ", startDate=" + startDate + "]";
	}
}
