package iss.nus.edu.sg.sa4106.KebunJio.DAO;

import java.util.List;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Plant;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;

public class UserprofileDAO {
	public User user;
	public List<Plant> history;
	public long totalPlanted;
	public long totalHarvested;
	public long uniquePlantTypes;
	public List<String> speciesNames;
	
	public UserprofileDAO() {}
	
	public UserprofileDAO(User user,List<Plant> history,long totalPlanted,long totalHarvested, long uniquePlantTypes, List<String> speciesNames) {
		this.user=user;
		this.history=history;
		this.totalPlanted=totalPlanted;
		this.totalHarvested=totalHarvested;
		this.uniquePlantTypes=uniquePlantTypes;
		this.speciesNames=speciesNames;
	}
	
}
