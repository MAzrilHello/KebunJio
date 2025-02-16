package iss.nus.edu.sg.sa4106.KebunJio.DAO;

import java.util.List;
import java.util.Map;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Comment;
import iss.nus.edu.sg.sa4106.KebunJio.Models.CommentLike;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Post;

public class PostWithCommentDAO {
	public Post post;
	public List<Comment> commentList;
	public Map<String, List<CommentLike>> commentLikeList;
	
	public PostWithCommentDAO(Post post,List<Comment> commentList, Map<String, List<CommentLike>>commentLikeList){
		this.post=post;
		this.commentList=commentList;
		this.commentLikeList=commentLikeList;
	}
}
