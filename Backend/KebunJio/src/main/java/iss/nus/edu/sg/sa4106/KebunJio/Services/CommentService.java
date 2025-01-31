package iss.nus.edu.sg.sa4106.KebunJio.Services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iss.nus.edu.sg.sa4106.KebunJio.DAO.CommentDAO;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Comment;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.CommentRepository;

@Service
public class CommentService {
	@Autowired
	private CommentRepository commentRepository;
	
	public boolean createComment(CommentDAO newComment,String PostId,String UserId) {
		boolean result = false;
		
		Comment comment = new Comment();
		comment.setCommentContent(newComment.commentContent);
		comment.setLikeCount(0);
		comment.setDislikeCount(0);
		comment.setPostId(PostId);
		comment.setUserId(UserId);
		comment.setPublishedDateTime(LocalDateTime.now());
		
		try {
			commentRepository.save(comment);
			result=true;
		}catch(Exception e) {
			throw new RuntimeException("Create comment error");
		}
		
		return result;
		
	}
	
	public Comment getCommentByCommentId(String CommentId) {
		Optional<Comment> comment = commentRepository.findById(CommentId);
		if(comment.isPresent()) {
			return comment.get();
		}else {
			return null;
		}
	}
	
	public boolean calculateLikeCount(String CommentId,boolean hasLiked) {
		boolean result = false;
		Optional<Comment> comment = commentRepository.findById(CommentId);
		if(comment.isPresent()) {
			Comment newComment = comment.get();
			// updateLikeCount
			if(!hasLiked) {
				newComment.setLikeCount(newComment.getLikeCount()+1);
			}else {
				if(newComment.getLikeCount()-1<0) {
					throw new RuntimeException("Count can not less than 0");
				}else {
					newComment.setLikeCount(newComment.getLikeCount()-1);
				}
			}
			try {
				commentRepository.save(newComment);
				result = true;
			}catch(Exception e) {
				throw new RuntimeException("Update Error");
			}
		}
		
		return result;
	}
	
	public boolean calculateDislikeCount(String CommentId,boolean hasDisliked) {
		boolean result = false;
		Optional<Comment> comment = commentRepository.findById(CommentId);
		if(comment.isPresent()) {
			Comment newComment = comment.get();
			// updateDislikeCount
			if(!hasDisliked) {
				newComment.setDislikeCount(newComment.getDislikeCount()+1);
			}else {
				if(newComment.getDislikeCount()-1<0) {
					throw new RuntimeException("Count can not less than 0");
				}else {
					newComment.setDislikeCount(newComment.getDislikeCount()-1);
				}
			}
			try {
				commentRepository.save(newComment);
				result = true;
			}catch(Exception e) {
				throw new RuntimeException("Update Error");
			}
		}
		
		return result;
	}
	
	// updateCommentByCommentId
	public boolean updateCommentByCommentId(String id,String newContent) {
		boolean result = false;
		Optional<Comment> updateComment = commentRepository.findById(id);
		if(updateComment.isPresent()) {
			Comment newComment = updateComment.get();
			newComment.setCommentContent(newContent);
			// when comment update, the like count and dislike count should set on zero?
			try {
				commentRepository.save(newComment);
				result = true;
			}catch(Exception e) {
				throw new RuntimeException("Update Error");
			}
		}
		return result;
		
	}
	
	// getCommentsByPostId
	public List<Comment> getCommentsByPostId(String postId){
		List<Comment> commentList = commentRepository.findByPostId(postId);
		Collections.sort(commentList,(c1,c2)->Integer.compare(c2.getLikeCount(), c1.getLikeCount()));
		return commentList;
	}
	
	// deleteComment
	public boolean deleteCommentByCommentId(String id) {
		boolean result = false;
		if(commentRepository.existsById(id)) {
			commentRepository.deleteById(id);
			result = true;
		}
		return result;
	}
}
