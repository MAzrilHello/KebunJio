package iss.nus.edu.sg.sa4106.KebunJio.DAO;


import java.time.LocalDateTime;
import java.util.List;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Post;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Upvote;

public class PostWithUpvoteDAO {
	private Post post;
        private String username;
	private int upvoteCount;
	private int commentCount;
	private List<Upvote> upvotes;
	
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
	public List<Upvote> getUpvotes() {
		return upvotes;
	}
	public void setUpvotes(List<Upvote> upvotes) {
		this.upvotes = upvotes;
	}
}
