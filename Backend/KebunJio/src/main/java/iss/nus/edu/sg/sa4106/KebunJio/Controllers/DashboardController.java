package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import iss.nus.edu.sg.sa4106.KebunJio.Models.Event;
import iss.nus.edu.sg.sa4106.KebunJio.Models.Plant;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.PlantRepository;
import iss.nus.edu.sg.sa4106.KebunJio.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Dashboard")
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/statistics")
    public Map<String, Object> getStatistics() {
        Integer userCont = userRepository.findAll().size();
        List<Plant> plantList = plantRepository.findAll();
        int plateCount = plantList.size();
        int totalHarvested = plantList.stream().filter(Plant::getHarvested).toList().size();
        int totalDisease = plantList.stream().filter(item -> !StringUtils.isEmpty(item.getDisease()))
                .toList().size();
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalUsers", userCont);
        statistics.put("totalPlantsPlanted", plateCount);
        statistics.put("totalPlantsHarvested", totalHarvested);
        statistics.put("totalDiseasesReported", totalDisease);

        // 植物类型分布
        Map<String, Integer> popularPlantTypes = new HashMap<>();
        Map<String, List<Plant>> plateNameMap = plantList.stream().collect(Collectors.groupingBy(Plant::getName));
        plateNameMap.forEach((key, value) -> {
            popularPlantTypes.put(key, value.size());
        });

        // reportedDiseases
        Map<String, Integer> reportedDiseases = new HashMap<>();
        Map<String, List<Plant>> reportedDiseasesMap = plantList.stream().collect(Collectors.groupingBy(Plant::getDisease));
        reportedDiseasesMap.forEach((key, value) -> {
            reportedDiseases.put(key, value.size());
        });
        statistics.put("popularPlantTypes", popularPlantTypes);
        statistics.put("reportedDiseases", reportedDiseases);
        return statistics;
    }

    @PostConstruct
    public void init() {
        getStatistics();
    }


//    @PostConstruct
//    public void insertPlantData() {
//        // 插入 EdiblePlantSpecies 数据
//        List<EdiblePlantSpecies> speciesList = new ArrayList<>();
//        List<String> names = List.of("Chili", "Apple", "Cassava", "Grape", "Corn", "Pepper Bell", "Cherry", "Potato", "Cucumber", "Rice", "Tomato", "Guava", "Sugarcane", "Soybean", "Wheat", "Lemon", "Mango", "Peach", "Pomegranate", "Jamun", "Strawberry");
//        for (String name : names) {
//            EdiblePlantSpecies species = new EdiblePlantSpecies();
//            species.setName(name);
//            species.setScientificName(name + " sp.");
//            species.setDescription(name + " is a " + (name.length() > 5 ? "fruit" : "vegetable") + ".");
//            species.setWateringTips("Water regularly.");
//            species.setSunlight("Full sun");
//            species.setSoilType("Loamy");
//            species.setHarvestTime("60-90 days");
//            species.setCommonPests("Aphids");
//            species.setGrowingSpace("0.5 sq ft");
//            species.setFertilizerTips("Use organic fertilizer.");
//            species.setSpecialNeeds("None.");
//            species.setImageUrl("http://example.com/" + name.toLowerCase() + ".jpg");
//            speciesList.add(species);
//        }
//        for (EdiblePlantSpecies species : speciesList) {
//            mongoTemplate.save(species, "EdiblePlantSpecies");
//        }
//
//        // 插入 User 数据
//        List<User> userList = new ArrayList<>();
//        for (int i = 1; i <= 5; i++) {
//            User user = new User();
//            user.setId(String.valueOf(i)); // Set a unique ID for each user
//            user.setUsername("farmer" + i);
//            user.setEmail("farmer" + i + "@example.com");
//            user.setPhoneNumber("123456789" + i);
//            user.setAdmin(false);
//            user.setPassword("password" + i);
//            userList.add(user);
//        }
//        for (User user : userList) {
//            mongoTemplate.save(user, "Users");
//        }
//
//        // 插入 Plant 数据
//        Random random = new Random();
//        List<String> chiliDiseases = List.of(
//                "Chili white star disease", "Chili brown spot disease", "Chili anthracnose",
//                "Chili blight", "Chili downy mildew", "Chili white rust", "Chili leaf mold",
//                "Chili gray spot disease", "Chili virus disease"
//        );
//        List<String> plantNames = new ArrayList<>(names);
//        for (int i = 0; i < 10; i++) {
//            Plant plant = new Plant();
//            String plantName = plantNames.get(i % plantNames.size());
//            plant.setName(plantName + " Plant " + (i + 1));
//            plant.setDisease(chiliDiseases.get(random.nextInt(chiliDiseases.size())));
//            plant.setPlantedDate(LocalDateTime.now().minusDays(random.nextInt(30)));
//            plant.setHarvestStartDate(LocalDateTime.now().plusDays(random.nextInt(60)));
//            plant.setPlantHealth("Good");
//            plant.setHarvested(false);
//            plant.setEdiblePlantSpecies(speciesList.get(plantNames.indexOf(plantName)));
//            User user = userList.get(i % userList.size());
//            plant.setUser(user);
//
//            mongoTemplate.save(plant, "Plant");
//        }
//
//        System.out.println("10 Plant data inserted successfully.");
//
//    }


   // @PostConstruct
    public void insertEvents() {
        // 创建并插入第1条数据
        Event event1 = new Event();
        event1.setId("1");
        event1.setName("Spring Festival");
        event1.setLocation("Beijing");
        event1.setStartDateTime(LocalDateTime.of(2025, 2, 10, 8, 0));
        event1.setEndDateTime(LocalDateTime.of(2025, 2, 10, 17, 0));
        event1.setDescription("The Spring Festival in Beijing");
        event1.setPicture("https://example.com/spring_festival.jpg");
        mongoTemplate.save(event1, "Event");

        // 创建并插入第2条数据
        Event event2 = new Event();
        event2.setId("2");
        event2.setName("Summer Concert");
        event2.setLocation("Shanghai");
        event2.setStartDateTime(LocalDateTime.of(2025, 6, 20, 19, 0));
        event2.setEndDateTime(LocalDateTime.of(2025, 6, 20, 22, 0));
        event2.setDescription("A summer concert in Shanghai");
        event2.setPicture("https://example.com/summer_concert.jpg");
        mongoTemplate.save(event2, "Event");

        // 创建并插入第3条数据
        Event event3 = new Event();
        event3.setId("3");
        event3.setName("Autumn Fair");
        event3.setLocation("Guangzhou");
        event3.setStartDateTime(LocalDateTime.of(2025, 9, 15, 10, 0));
        event3.setEndDateTime(LocalDateTime.of(2025, 9, 15, 18, 0));
        event3.setDescription("An autumn fair in Guangzhou");
        event3.setPicture("https://example.com/autumn_fair.jpg");
        mongoTemplate.save(event3, "Event");

        // 创建并插入第4条数据
        Event event4 = new Event();
        event4.setId("4");
        event4.setName("Winter Carnival");
        event4.setLocation("Harbin");
        event4.setStartDateTime(LocalDateTime.of(2025, 12, 25, 9, 0));
        event4.setEndDateTime(LocalDateTime.of(2025, 12, 25, 16, 0));
        event4.setDescription("A winter carnival in Harbin");
        event4.setPicture("https://example.com/winter_carnival.jpg");
        mongoTemplate.save(event4, "Event");

        // 创建并插入第5条数据
        Event event5 = new Event();
        event5.setId("5");
        event5.setName("Tech Conference");
        event5.setLocation("Shenzhen");
        event5.setStartDateTime(LocalDateTime.of(2025, 3, 10, 9, 0));
        event5.setEndDateTime(LocalDateTime.of(2025, 3, 10, 17, 0));
        event5.setDescription("A tech conference in Shenzhen");
        event5.setPicture("https://example.com/tech_conference.jpg");
        mongoTemplate.save(event5, "Event");

        // 创建并插入第6条数据
        Event event6 = new Event();
        event6.setId("6");
        event6.setName("Art Exhibition");
        event6.setLocation("Chengdu");
        event6.setStartDateTime(LocalDateTime.of(2025, 4, 20, 10, 0));
        event6.setEndDateTime(LocalDateTime.of(2025, 4, 20, 18, 0));
        event6.setDescription("An art exhibition in Chengdu");
        event6.setPicture("https://example.com/art_exhibition.jpg");
        mongoTemplate.save(event6, "Event");

        // 创建并插入第7条数据
        Event event7 = new Event();
        event7.setId("7");
        event7.setName("Music Festival");
        event7.setLocation("Hangzhou");
        event7.setStartDateTime(LocalDateTime.of(2025, 7, 15, 18, 0));
        event7.setEndDateTime(LocalDateTime.of(2025, 7, 15, 23, 0));
        event7.setDescription("A music festival in Hangzhou");
        event7.setPicture("https://example.com/music_festival.jpg");
        mongoTemplate.save(event7, "Event");

        // 创建并插入第8条数据
        Event event8 = new Event();
        event8.setId("8");
        event8.setName("Sports Event");
        event8.setLocation("Nanjing");
        event8.setStartDateTime(LocalDateTime.of(2025, 8, 20, 9, 0));
        event8.setEndDateTime(LocalDateTime.of(2025, 8, 20, 17, 0));
        event8.setDescription("A sports event in Nanjing");
        event8.setPicture("https://example.com/sports_event.jpg");
        mongoTemplate.save(event8, "Event");

        // 创建并插入第9条数据
        Event event9 = new Event();
        event9.setId("9");
        event9.setName("Food Festival");
        event9.setLocation("Chongqing");
        event9.setStartDateTime(LocalDateTime.of(2025, 10, 30, 10, 0));
        event9.setEndDateTime(LocalDateTime.of(2025, 10, 30, 18, 0));
        event9.setDescription("A food festival in Chongqing");
        event9.setPicture("https://example.com/food_festival.jpg");
        mongoTemplate.save(event9, "Event");

        // 创建并插入第10条数据
        Event event10 = new Event();
        event10.setId("10");
        event10.setName("Film Premiere");
        event10.setLocation("Xiamen");
        event10.setStartDateTime(LocalDateTime.of(2025, 11, 25, 19, 0));
        event10.setEndDateTime(LocalDateTime.of(2025, 11, 25, 22, 0));
        event10.setDescription("A film premiere in Xiamen");
        event10.setPicture("https://example.com/film_premiere.jpg");
        mongoTemplate.save(event10, "Event");
    }

} 