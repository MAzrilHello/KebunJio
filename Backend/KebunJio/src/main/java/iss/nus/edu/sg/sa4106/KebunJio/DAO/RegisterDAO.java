package iss.nus.edu.sg.sa4106.KebunJio.DAO;


public class RegisterDAO {
	public String email;
	public String username;
	public String password;
	public String confirmPassword;
	public String contactPhone;
	
	public RegisterDAO() {}
	
	public RegisterDAO(String email,String username,String password,String confirmPassword,String contactPhone) {
		this.email=email;
		this.confirmPassword=confirmPassword;
		this.username = username;
		this.password = password;
		this.contactPhone = contactPhone;
	}
}
