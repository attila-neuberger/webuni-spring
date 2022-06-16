package hu.webuni.hr.comtur.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import hu.webuni.hr.comtur.dto.IDtoKey;

@Entity
public class Company implements IDtoKey {

	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable = false)
	private long companyRegistrationNumber;
	
	@Column(nullable = false)
	private String name;
	
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private CompanyType companyType;
	
	@Column(nullable = false)
	private String address;
	
	@OneToMany(mappedBy = "company")
	private List<Employee> employees;

	public Company() {
		this.employees = new ArrayList<>();
	}

	public Company(long companyRegistrationNumber, String name, CompanyType companyType, String address) {
		this.companyRegistrationNumber = companyRegistrationNumber;
		this.name = name;
		this.companyType = companyType;
		this.address = address;
		this.employees = new ArrayList<>();
	}
	
	public Company(long id, long companyRegistrationNumber, String name, CompanyType companyType, String address, 
			List<Employee> employees) {
		this.id = id;
		this.companyRegistrationNumber = companyRegistrationNumber;
		this.name = name;
		this.companyType = companyType;
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
	
	public CompanyType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(CompanyType companyType) {
		this.companyType = companyType;
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
	
	public void addEmployee(Employee employee) {
		if (employees == null) {
			employees = new ArrayList<>();
		}
		employees.add(employee);
		employee.setCompany(this);
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
		Company other = (Company) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", companyRegistrationNumber=" + companyRegistrationNumber + ", name=" + name
				+ ", companyType=" + companyType + ", address=" + address + "]";
	}
}
