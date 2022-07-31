package hu.webuni.logistics.comtur.dto;

import javax.validation.constraints.NotEmpty;

/**
 * Login data transfer object.
 * 
 * @author comtur
 */
public class LoginDto {

	@NotEmpty
	private String userName;

	@NotEmpty
	private String password;

	public LoginDto() {
		super();
	}

	public LoginDto(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
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
}
