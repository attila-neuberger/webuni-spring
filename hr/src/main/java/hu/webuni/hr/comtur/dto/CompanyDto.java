package hu.webuni.hr.comtur.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;

import hu.webuni.hr.comtur.dto.Views.VisibleData;
import hu.webuni.hr.comtur.model.CompanyType;

public class CompanyDto implements IDtoKey {
	
	@JsonView(VisibleData.class)
	private long id;
	
	@JsonView(VisibleData.class)
	private long companyRegistrationNumber;
	
	@JsonView(VisibleData.class)
	private String name;
	
	/*@JsonView(VisibleData.class)
	private CompanyType companyType;*/
	
	@JsonView(VisibleData.class)
	private Set<CompanyType> companyTypes;
	
	@JsonView(VisibleData.class)
	private String address;
	
	private List<EmployeeDto> employees;

	public CompanyDto() {
		this.employees = new ArrayList<>();
	}

	public CompanyDto(long id, long companyRegistrationNumber, String name, CompanyType companyType, String address, 
			List<EmployeeDto> employees) {
		this.id = id;
		this.companyRegistrationNumber = companyRegistrationNumber;
		this.name = name;
		// this.companyType = companyType;
		this.companyTypes = new HashSet<>(1);
		companyTypes.add(companyType);
		this.address = address;
		this.employees = employees == null ? new ArrayList<>() : employees;
	}
	
	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
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
	
	/*public CompanyType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(CompanyType companyType) {
		this.companyType = companyType;
	}*/

	public Set<CompanyType> getCompanyTypes() {
		return companyTypes;
	}

	public void setCompanyTypes(Set<CompanyType> companyTypes) {
		this.companyTypes = companyTypes;
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
		CompanyDto other = (CompanyDto) obj;
		return id == other.id;
	}
}
