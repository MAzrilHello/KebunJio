package iss.nus.edu.sg.sa4106.KebunJio.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Upvote;

public interface UpvoteRepository extends MongoRepository<Upvote,String> {
	@Query("{'postId':?0}")
	List<Upvote> findByPostId(String postId);
	
	@Query("{'postId':?0,'userId':?1}")
	Optional<Upvote> findUpvote(String postId,String userId);
}
