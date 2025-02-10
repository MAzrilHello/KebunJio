package iss.nus.edu.sg.sa4106.KebunJio.DAO;

import java.util.List;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Comment;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Post;

public class PostWithCommentDAO {
	public Post post;
	public List<Comment> commentList;
	public String avatarUrl;  // 添加用户头像字段

	public PostWithCommentDAO(Post post, List<Comment> commentList, String avatarUrl) {
		this.post = post;
		this.commentList = commentList;
		this.avatarUrl = avatarUrl;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}
}
