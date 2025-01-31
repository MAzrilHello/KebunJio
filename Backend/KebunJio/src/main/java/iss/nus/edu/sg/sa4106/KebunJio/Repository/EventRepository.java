package iss.nus.edu.sg.sa4106.KebunJio.Repository;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
}