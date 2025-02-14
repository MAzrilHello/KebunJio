package iss.nus.edu.sg.sa4106.KebunJio.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Upvote")
public class Upvote {
	@Id
	private String id;
	
	private String postId;
	
	private String userId;
	
	private boolean hasUpvoted = false;
	
	public Upvote() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public boolean isHasUpvoted() {
		return hasUpvoted;
	}

	public void setHasUpvoted(boolean hasUpvoted) {
		this.hasUpvoted = hasUpvoted;
	}
	
	
}
