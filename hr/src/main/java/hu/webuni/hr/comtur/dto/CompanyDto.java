package hu.webuni.hr.comtur.dto;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import hu.webuni.hr.comtur.dto.Views.VisibleData;

public class CompanyDto implements IDtoKey {
	
	@JsonView(VisibleData.class)
	private long companyRegistrationNumber;
	
	@JsonView(VisibleData.class)
	private String name;
	
	@JsonView(VisibleData.class)
	private String address;
	
	private List<EmployeeDto> employees;

	public CompanyDto() {}

	public CompanyDto(long companyRegistrationNumber, String name, String address, List<EmployeeDto> employees) {
		this.companyRegistrationNumber = companyRegistrationNumber;
		this.name = name;
		this.address = address;
		this.employees = employees;
	}

	public long getCompanyRegistrationNumber() {
		return companyRegistrationNumber;
	}
	public void setCompanyRegistrationNumber(long companyRegistrationNumber) {
		this.companyRegistrationNumber = companyRegistrationNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<EmployeeDto> getEmployees() {
		return employees;
	}
	public void setEmployees(List<EmployeeDto> employees) {
		this.employees = employees;
	}

	@Override
	@JsonIgnore
	public long getId() {
		return companyRegistrationNumber;
	}

	@Override
	public void setId(long id) {
		this.companyRegistrationNumber = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(companyRegistrationNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompanyDto other = (CompanyDto) obj;
		return companyRegistrationNumber == other.companyRegistrationNumber;
	}
}
