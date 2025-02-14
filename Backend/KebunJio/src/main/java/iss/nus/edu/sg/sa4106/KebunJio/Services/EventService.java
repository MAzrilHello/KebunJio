package iss.nus.edu.sg.sa4106.KebunJio.Services;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Event;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.EventRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Event> findAll() {
        return eventRepository.findAll();
    }
    
    public Page<Event> findByPages(String name, String date, Pageable pageable) {
        // 构建查询条件
        Query query = new Query();
        if (name != null && !name.trim().isEmpty()) {
            query.addCriteria(Criteria.where("name").regex(name, "i")); // 模糊匹配，不区分大小写
        }
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate targetDate = LocalDate.parse(date,formatter);
            LocalDateTime startOfDay = LocalDateTime.of(targetDate, LocalTime.MIN); // 当天的开始时间（00:00:00）
            LocalDateTime startOfNextDay = startOfDay.plusDays(1); // 第二天的开始时间
            query.addCriteria(Criteria.where("startDateTime").gte(startOfDay).lt(startOfNextDay));
        }

        // 添加分页逻辑
        query.with(pageable);

        // 执行查询
        List<Event> result = mongoTemplate.find(query, Event.class);

        // 计算总记录数
        long total = mongoTemplate.count(query, Event.class);

        // 构建 Page 对象
        Page<Event> pageResult = new PageImpl<>(result, pageable, total);

        return pageResult;
    }
    
    // Change the Id to String
    public Event findByEventId(String eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public boolean deleteByEventId(String eventId) {
    	Event theEvent = findByEventId(eventId);
    	if (theEvent != null) {
    		eventRepository.delete(theEvent);
    		return true;
    	}
    	return false;
        //eventRepository.deleteByEventId(eventId);
    }

    public boolean createEvent(Event event, String authorizationCode) {
        try {
            return googleCalendarService.addEventToCalendar(authorizationCode, event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create event", e);
        }
    }

    public Event getEvent(Long id) {
        return null;
    }

    public void updateEvent(Event event) {
    }

    public void deleteEvent(Long id) {
    }
}
