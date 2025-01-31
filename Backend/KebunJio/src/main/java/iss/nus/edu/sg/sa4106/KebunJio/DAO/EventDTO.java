package com.plant.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class EventDTO {
    @NotBlank(message = "Event name is required")
    private String name;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    
    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;
    
    private String picture;
} 