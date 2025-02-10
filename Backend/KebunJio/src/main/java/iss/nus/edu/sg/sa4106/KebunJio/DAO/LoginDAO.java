package iss.nus.edu.sg.sa4106.KebunJio.DAO;

public class LoginDAO {
	public String emailOrUsername;
	public String password;
	
	public LoginDAO() {}
	
	public LoginDAO(String emailOrUsername,String password) {
		this.emailOrUsername=emailOrUsername;
		this.password = password;
	}
}
