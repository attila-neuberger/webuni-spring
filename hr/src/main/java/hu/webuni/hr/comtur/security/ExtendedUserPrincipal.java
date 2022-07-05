package hu.webuni.hr.comtur.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import hu.webuni.hr.comtur.model.Employee;

public class ExtendedUserPrincipal extends User {

	private static final long serialVersionUID = 1L;
	
	private Employee employee;

	public ExtendedUserPrincipal(Employee employee, Collection<? extends GrantedAuthority> authorities) {
		super(employee.getUserName(), employee.getPassword(), authorities);
		this.employee = employee;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public String toString() {
		return String.format("ExtendedUserPrincipal [%s, Employee= %s]", super.toString(), employee.toString());
	}
}
