package iss.nus.edu.sg.sa4106.KebunJio.DAO;

public class UserEditDAO {
	public String username;
	public String email;
	public String phoneNumber;
	
	public UserEditDAO() {}
	
	public UserEditDAO(String username,String email, String phoneNumber) {
		this.username=username;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}
}

