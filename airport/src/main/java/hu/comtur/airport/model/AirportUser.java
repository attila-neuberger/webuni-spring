package hu.comtur.airport.model;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

@Entity
public class AirportUser {

	@Id
	private String userName;
	
	private String password;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> roles;

	public AirportUser() {
		super();
	}

	public AirportUser(String userName, String password, Set<String> roles) {
		super();
		this.userName = userName;
		this.password = password;
		this.roles = roles;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
}
