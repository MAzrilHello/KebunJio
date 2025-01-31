package iss.nus.edu.sg.sa4106.KebunJio.Repository;
import iss.nus.edu.sg.sa4106.KebunJio.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User,String> {

    User findByUsername(String username);
    User findByEmail(String email);


}
