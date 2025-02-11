package iss.nus.edu.sg.sa4106.KebunJio.DAO;

import java.util.List;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Plant;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;

public class UserprofileDAO {
	public User user;
	public List<Plant> history;
	public long totalPlanted;
	public long uniquePlantTypes;
	public long totalHarvested;
	
	public UserprofileDAO() {}
	
	public UserprofileDAO(User user,List<Plant> history,long totalPlanted,long uniquePlantTypes,long totalHarvested) {
		this.user=user;
		this.history=history;
		this.totalPlanted=totalPlanted;
		this.uniquePlantTypes=uniquePlantTypes;
		this.totalHarvested=totalHarvested;
	}
	
}
