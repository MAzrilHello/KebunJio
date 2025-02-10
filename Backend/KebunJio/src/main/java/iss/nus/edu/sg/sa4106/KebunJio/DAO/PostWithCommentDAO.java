package iss.nus.edu.sg.sa4106.KebunJio.DAO;

import java.util.List;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Comment;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Post;

public class PostWithCommentDAO {
	public Post post;
	public List<Comment> commentList;
	
	public PostWithCommentDAO(Post post,List<Comment> commentList){
		this.post=post;
		this.commentList=commentList;
	}
}
