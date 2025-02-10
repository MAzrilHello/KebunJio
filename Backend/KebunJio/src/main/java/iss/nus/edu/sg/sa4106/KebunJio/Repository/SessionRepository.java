package iss.nus.edu.sg.sa4106.KebunJio.Repository;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SessionRepository extends MongoRepository<Session, String> {
	@Query("{'id': ?0}")
	Optional<Session> findById(String id);
    //@Query("SELECT s, u FROM Session s JOIN s.user u WHERE s.sessionId = :sessionId")
	//@Query("{'sessionId': ?0}")
	//Optional<Object[]> findUserAndSessionBySessionId(int sessionId);
} 