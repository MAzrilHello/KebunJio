package iss.nus.edu.sg.sa4106.KebunJio.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
public class User {
	@Id

	private String userId;

	@Indexed(unique = true)
	private String username;

	@Indexed(unique = true)
	private String email;

	private String phoneNumber;
	private boolean isAdmin;
	private String password;

	public User() {}

	public String getId() {
		return userId;
	}

	public void setId(String userId) {
		this.userId = userId;

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getPassword(){return password;}

	public void setPassword(String password){this.password=password;}
	
	
	

}
