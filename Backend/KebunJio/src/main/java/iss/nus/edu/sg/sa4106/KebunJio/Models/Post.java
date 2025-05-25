package iss.nus.edu.sg.sa4106.KebunJio.Models;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;

/* ToDiscuss:
 * 1. Confirm which format we want to use for the relationship
 * 2. Use the mongoDB generate Id or custom Id?*/
@Document(collection="Post")
public class Post {
	@Id
	private String id;
	
	private String userId;
	
	@NotNull(message="Title can not be null")
	private String title;
	@NotNull(message="Content can not be null")
	private String content;
	
	private String postCategory;
	
	private LocalDateTime publishedDateTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Post() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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



	public LocalDateTime getPublishedDateTime() {
		return publishedDateTime;
	}

	public void setPublishedDateTime(LocalDateTime publishedDateTime) {
		this.publishedDateTime = publishedDateTime;
	}

	public String getPostCategory() {
		return postCategory;
	}

	public void setPostCategory(String postCategory) {
		this.postCategory = postCategory;
	}



}
