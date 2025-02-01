package iss.nus.edu.sg.sa4106.KebunJio.Models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Comment")
public class Comment {
	@Id
	private String id;

	private String postId;
	private String userId;
	private String commentContent;
	private int likeCount;
	private int dislikeCount;
	private LocalDateTime publishedDateTime;
	
	public Comment() {}

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

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getDislikeCount() {
		return dislikeCount;
	}

	public void setDislikeCount(int dislikeCount) {
		this.dislikeCount = dislikeCount;
	}

	public LocalDateTime getPublishedDateTime() {
		return publishedDateTime;
	}

	public void setPublishedDateTime(LocalDateTime publishedDateTime) {
		this.publishedDateTime = publishedDateTime;
	}


	
	



}
