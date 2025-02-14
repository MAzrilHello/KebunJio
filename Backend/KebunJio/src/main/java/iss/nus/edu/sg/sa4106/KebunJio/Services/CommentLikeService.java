
package iss.nus.edu.sg.sa4106.KebunJio.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iss.nus.edu.sg.sa4106.KebunJio.Models.CommentLike;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.CommentLikeRepository;

@Service
public class CommentLikeService {
	@Autowired
	private CommentLikeRepository clRepository;
	
	//function1: get the likeCount By comment
	public Map<String,Integer> getLikeAndDislikeCountByComment(String commentId) {
		List<CommentLike> commentLikeList = clRepository.findByCommentId(commentId);
		Map<String,Integer> result = new HashMap<>();
		
		int likeCount =0;
		int dislikeCount =0;
		for(CommentLike commentLike : commentLikeList) {
			if(commentLike.isLike()) {
				likeCount++;
			}else if(commentLike.isDislike()) {
				dislikeCount++;
			}
		}
		result.put("likeCount",likeCount);
		result.put("dislikeCount",dislikeCount);
		
		return result;
	}
	
	// Function: get commentLikeByUserandComment
	public CommentLike getCommentLikeByUserAndComment(String userId,String commentId) {
		Optional<CommentLike> clOp = clRepository.findByUserIdAndCommentId(userId, commentId);
		if(clOp.isPresent()) {
			CommentLike cl = clOp.get();
			return cl;
		}
		return null;
	}
	
	
	// function boolean createCommentLike
	public boolean createCommentLike(String userId,String commentId,String likeState) {
		boolean result = false;
		
		CommentLike cl = new CommentLike();
		cl.setUserId(userId);
		cl.setCommentId(commentId);
		if(likeState.equals("like")) {
			cl.setLike(true);
		}else if (likeState.equals("dislike")) {
			cl.setDislike(true);
		}
		
		try {
			clRepository.save(cl);
			result = true;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Create CommentLike Error");
		}
		
		return result;
	}
	
	//Function Update
	public boolean updateCommentLike(CommentLike currentCL,String likeStatus,boolean status) {
		boolean result = false;
		Optional<CommentLike> updateCLOp = clRepository.findById(currentCL.getId());
		if(updateCLOp.isPresent()) {
			CommentLike updateCL = updateCLOp.get();
			switch (likeStatus) {
			case "like":
				updateCL.setLike(status);
				try {
					clRepository.save(updateCL);
					result = true;
				}catch(Exception e) {
					e.printStackTrace();
					throw new RuntimeException("Update like error");
				}
				
				break;
			case "dislike":
				updateCL.setDislike(status);
				try {
					clRepository.save(updateCL);
					result = true;
				}catch(Exception e) {
					e.printStackTrace();
					throw new RuntimeException("Update dislike error");
				}
				
				break;
			}
		
		}
		return result;
	}
}
