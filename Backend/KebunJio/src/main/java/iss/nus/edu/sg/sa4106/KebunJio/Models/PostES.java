package iss.nus.edu.sg.sa4106.KebunJio.Models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.validation.constraints.NotNull;

//ElasticSearch Class
@Document(indexName ="post_index")
public class PostES {
	@Id
	private String id;
	
	@Field(type=FieldType.Text,analyzer="standard")
	private String title;
	
	@Field(type=FieldType.Text,analyzer="standard")
	private String content;
	
	private String postCategory;
	
	@Field(type=FieldType.Date,format=DateFormat.date_hour_minute_second, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime publishedDateTime;
	
	private String userId;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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
