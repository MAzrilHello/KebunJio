package iss.nus.edu.sg.sa4106.KebunJio.DAO;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Post;

public class PostWithUpvoteDAO {
	private Post post;
	private int upvoteCount;
	
	public PostWithUpvoteDAO(Post post, int upvoteCount) {
		this.post = post;
		this.upvoteCount = upvoteCount;
	}
	
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public int getUpvoteCount() {
		return upvoteCount;
	}
	public void setUpvoteCount(int upvoteCount) {
		this.upvoteCount = upvoteCount;
	}
}
