package iss.nus.edu.sg.sa4106.KebunJio.DAO;


import java.time.LocalDateTime;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Post;

public class PostWithUpvoteDAO {
	private Post post;
        private String username;
	private int upvoteCount;
	private int commentCount;
	
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post=post;
	}
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public int getUpvoteCount() {
		return upvoteCount;
	}
	public void setUpvoteCount(int upvoteCount) {
		this.upvoteCount = upvoteCount;
	}
}
