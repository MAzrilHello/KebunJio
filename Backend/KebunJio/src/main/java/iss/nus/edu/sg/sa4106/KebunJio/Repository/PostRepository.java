package iss.nus.edu.sg.sa4106.KebunJio.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Post;

public interface PostRepository extends MongoRepository<Post,String> {
	@Query("{'userId': ?0}")
	List<Post> findByUserId(String userId);
}
