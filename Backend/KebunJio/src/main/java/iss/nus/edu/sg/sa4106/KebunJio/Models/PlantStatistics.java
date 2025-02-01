package com.plant.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document(collection = "plant_statistics")
public class PlantStatistics {
    @Id
    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    @Field("total_users")
    private Integer totalUsers;

    @Field("total_plants_planted")
    private Integer totalPlantsPlanted;

    @Field("total_plants_harvested")
    private Integer totalPlantsHarvested;

    @Field("total_diseases_reported")
    private Integer totalDiseasesReported;

    @Field("popular_plant_types")
    private Map<String, Integer> popularPlantTypes;

    @Field("reported_diseases")
    private Map<String, Integer> reportedDiseases;
} 