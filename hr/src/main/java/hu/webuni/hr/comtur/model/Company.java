package hu.webuni.hr.comtur.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;

@Entity
@NamedEntityGraph(
	name = "Company.full",
	attributeNodes = {
		@NamedAttributeNode(value = "employees", subgraph = "employees-subgraph"),
		@NamedAttributeNode("companyType")
	},
	subgraphs = {
		@NamedSubgraph(
			name = "employees-subgraph",
			attributeNodes = {
				@NamedAttributeNode("position")
			}
		)
	}
)
public class Company extends Id {

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
		super();
		this.employees = new ArrayList<>();
	}

	public Company(long companyRegistrationNumber, String name, CompanyType companyType, String address) {
		super();
		this.companyRegistrationNumber = companyRegistrationNumber;
		this.name = name;
		this.companyType = companyType;
		this.address = address;
		this.employees = new ArrayList<>();
	}
	
	public Company(long id, long companyRegistrationNumber, String name, CompanyType companyType, String address, 
			List<Employee> employees) {
		super(id);
		this.companyRegistrationNumber = companyRegistrationNumber;
		this.name = name;
		this.companyType = companyType;
		this.address = address;
		this.employees = employees == null ? new ArrayList<>() : employees;
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
	public String toString() {
		return "Company [id=" + getId() + ", companyRegistrationNumber=" + companyRegistrationNumber + ", name=" + name
				+ ", companyType=" + companyType + ", address=" + address + "]";
	}
}
