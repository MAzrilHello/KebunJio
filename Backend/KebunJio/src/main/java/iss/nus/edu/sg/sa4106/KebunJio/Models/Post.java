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
	@NotNull(message="Title can not be null")
	private String title;
	@NotNull(message="Content can not be null")
	private String content;
	private String questionStatus;
	private LocalDateTime publishedDateTime;
	private Boolean answerSolved;
	private String userId;
	private int upvote;
	
	public int getUpvote() {
		return upvote;
	}
	
	public void setUpvote(int upvote) {
		this.upvote = upvote;
	}
	
	

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

	public String getQuestionStatus() {
		return questionStatus;
	}

	public void setQuestionStatus(String questionStatus) {
		this.questionStatus = questionStatus;
	}

	public LocalDateTime getPublishedDateTime() {
		return publishedDateTime;
	}

	public void setPublishedDateTime(LocalDateTime publishedDateTime) {
		this.publishedDateTime = publishedDateTime;
	}

	public Boolean getAnswerSolved() {
		return answerSolved;
	}

	public void setAnswerSolved(Boolean answerSolved) {
		this.answerSolved = answerSolved;
	}

}
