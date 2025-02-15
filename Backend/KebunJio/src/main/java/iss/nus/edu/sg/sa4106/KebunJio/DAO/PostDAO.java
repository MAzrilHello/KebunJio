package iss.nus.edu.sg.sa4106.KebunJio.DAO;

import jakarta.validation.constraints.NotNull;

/* ToDiscuss:
 * 1. What Data need to transfer from Frontend,(based on UI)*/
public class PostDAO {
	@NotNull(message="Title can not be null")
	public String title;
	@NotNull(message="Title can not be null")
	public String content;
	@NotNull
	public String postCategory;
	
}
