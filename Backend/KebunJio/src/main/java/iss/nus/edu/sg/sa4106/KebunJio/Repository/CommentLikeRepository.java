package iss.nus.edu.sg.sa4106.KebunJio.Repository;

import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import iss.nus.edu.sg.sa4106.KebunJio.Models.CommentLike;

public interface CommentLikeRepository extends MongoRepository<CommentLike,String>{
	@Query("{'commentId':?0}")
	List<CommentLike> findByCommentId(String commentId);
	
	@Query("{'userId':?0,'commentId':?1}")
	Optional<CommentLike> findByUserIdAndCommentId(String userId,String commentId);
}
