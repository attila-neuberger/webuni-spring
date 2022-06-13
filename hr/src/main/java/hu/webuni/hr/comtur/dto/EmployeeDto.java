package hu.webuni.hr.comtur.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonView;

import hu.webuni.hr.comtur.dto.Views.VisibleData;

public class EmployeeDto implements IDtoKey, Comparable<EmployeeDto> {

	@JsonView(VisibleData.class)
	private long id;
	
	@JsonView(VisibleData.class)
	@NotEmpty
	private String name;
	
	/*@JsonView(VisibleData.class)
	@NotEmpty
	private String title;*/
	
	@JsonView(VisibleData.class)
	@ManyToOne
	@JoinColumn(name = "position_id")
	private PositionDto title;
	
	@JsonView(VisibleData.class)
	@Positive
	private int salary;
	
	@JsonView(VisibleData.class)
	@Past
	private LocalDateTime startDate;
	
	private CompanyDto company;
	
	public EmployeeDto() {}
	
	public EmployeeDto(long id, @NotEmpty String name, PositionDto title, @Positive int salary,
			@Past LocalDateTime startDate) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
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
	
	public PositionDto getTitle() {
		return title;
	}
	
	public void setTitle(PositionDto title) {
		this.title = title;
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

	public CompanyDto getCompany() {
		return company;
	}

	public void setCompany(CompanyDto company) {
		this.company = company;
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
