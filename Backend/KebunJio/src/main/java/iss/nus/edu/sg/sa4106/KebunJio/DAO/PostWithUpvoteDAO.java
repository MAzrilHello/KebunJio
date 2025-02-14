package iss.nus.edu.sg.sa4106.KebunJio.DAO;

import java.time.LocalDateTime;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Post;

public class PostWithUpvoteDAO {
	private String id;
	private String userId;
	private String username;
	private String title;
	private String content;
	private String postCategory;
	private LocalDateTime publishedDateTime;
	private int upvoteCount;
	private int commentCount;
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPostCategory() {
		return postCategory;
	}
	public void setPostCategory(String postCategory) {
		this.postCategory = postCategory;
	}
	public LocalDateTime getPublishedDateTime() {
		return publishedDateTime;
	}
	public void setPublishedDateTime(LocalDateTime publishedDateTime) {
		this.publishedDateTime = publishedDateTime;
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
