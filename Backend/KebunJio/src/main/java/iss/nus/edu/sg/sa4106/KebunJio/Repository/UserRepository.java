package iss.nus.edu.sg.sa4106.KebunJio.Repository;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface UserRepository extends MongoRepository<User,String> {

	@Query("{ 'username': { $regex: ?0, $options: 'i' } }")
    User findByUsername(String username);
	@Query("{ 'email': { $regex: ?0, $options: 'i' } }")
    User findByEmail(String email);
    Optional<User> findById(String id);


}
