package hu.webuni.hr.comtur.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;

public class EmployeeDto implements IDtoKey, Comparable<EmployeeDto> {

	private long id;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String position;
	
	@Positive
	private int salary;
	
	@Past
	private LocalDateTime startDate;
	
	public EmployeeDto() {}
	
	public EmployeeDto(long id, String name, String position, int salary, LocalDateTime startDate) {
		this.id = id;
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.startDate = startDate;
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
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
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

	@Override
	public int compareTo(EmployeeDto o) {
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
		EmployeeDto other = (EmployeeDto) obj;
		return id == other.id;
	}
}
