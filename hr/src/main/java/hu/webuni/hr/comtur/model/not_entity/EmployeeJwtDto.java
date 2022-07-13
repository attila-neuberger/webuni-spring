package hu.webuni.hr.comtur.model.not_entity;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeJwtDto {

	@JsonProperty("user_id")
	private long id;
	
	@JsonProperty("user_name")
	private String userName;
	
	public EmployeeJwtDto() {}
	
	public EmployeeJwtDto(long id, String userName) {
		this.id = id;
		this.userName = userName;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
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
		EmployeeJwtDto other = (EmployeeJwtDto) obj;
		return id == other.id;
	}
}
