package iss.nus.edu.sg.sa4106.KebunJio.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Comment;

public interface CommentRepository extends MongoRepository<Comment,String> {
	@Query("{'postId':?0}")
	List<Comment> findByPostId(String postId);
}
