package iss.nus.edu.sg.sa4106.KebunJio.Repository;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    Optional<Event> findById(String eventId);
    //void deleteById(String eventId);

    // 自定义分页查询方法
    Page<Event> findByNameContainingIgnoreCaseAndStartDateTime(String name, String startDateTime, Pageable pageable);
}
