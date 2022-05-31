package hu.webuni.hr.comtur.model;

import java.util.List;
import java.util.Objects;

import hu.webuni.hr.comtur.dto.IDtoKey;

public class Company implements IDtoKey {

	private long companyRegistrationNumber;
	
	private String name;
	
	private String address;
	
	private List<Employee> employees;

	public Company() {}

	public Company(long companyRegistrationNumber, String name, String address, List<Employee> employees) {
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
	public List<Employee> getEmployees() {
		return employees;
	}
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
	@Override
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
		Company other = (Company) obj;
		return companyRegistrationNumber == other.companyRegistrationNumber;
	}
}
