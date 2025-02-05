package iss.nus.edu.sg.sa4106.KebunJio.Models;

import lombok.Data;  // Lombok for getters, setters, etc.
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonProperty;  // Jackson annotations
import java.util.Map;

@Document(collection = "plant_statistics")  // MongoDB collection name
@Data  // Lombok to automatically generate getters, setters, toString(), equals(), hashCode()
public class PlantStatistics {

    @Id
    private String id;

    @JsonProperty("date")  // Use @JsonProperty to ensure it serializes correctly to JSON
    private String date;

    @JsonProperty("total_users")  // Ensure this field is serialized as 'total_users' in JSON
    private Integer totalUsers;

    @JsonProperty("total_plants_planted")
    private Integer totalPlantsPlanted;

    @JsonProperty("total_plants_harvested")
    private Integer totalPlantsHarvested;

    @JsonProperty("total_diseases_reported")
    private Integer totalDiseasesReported;

    @JsonProperty("popular_plant_types")
    private Map<String, Integer> popularPlantTypes;

    @JsonProperty("reported_diseases")
    private Map<String, Integer> reportedDiseases;
}
