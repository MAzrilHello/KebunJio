package iss.nus.edu.sg.sa4106.KebunJio.Controllers;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import iss.nus.edu.sg.sa4106.KebunJio.Models.PlantHealthCheckImage;
import iss.nus.edu.sg.sa4106.KebunJio.Services.PlantHealthCheckService;

@RestController
@RequestMapping("/Healthcheck")
@CrossOrigin(origins = "*")
public class PlantHealthCheckController {
	
    @Autowired
    private PlantHealthCheckService plantHealthCheckService;
    
    @PostMapping
    public ResponseEntity<PlantHealthCheckImage> uploadPlantImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId,
            @RequestParam("plantId") String plantId,
            @RequestParam("imageSource") String imageSource) {
    	
        String imageUrl = "https://storage.example.com/" + file.getOriginalFilename(); 
        
        String dummyPlantHealth = "Healthy";
        
        PlantHealthCheckImage plantHealthCheckImage = new PlantHealthCheckImage();
        plantHealthCheckImage.setId(UUID.randomUUID().toString());
        plantHealthCheckImage.setUserId(userId);
        plantHealthCheckImage.setPlantId(plantId);
        plantHealthCheckImage.setImageUrl(imageUrl);
        plantHealthCheckImage.setImageSource(imageSource);
        plantHealthCheckImage.setPlantHealth(dummyPlantHealth);
        plantHealthCheckImage.setCreatedDateTime(LocalDateTime.now());

        plantHealthCheckService.saveHealthCheck(plantHealthCheckImage);

        System.out.println("Saved PlantHealthCheckImage: " + plantHealthCheckImage);

        return new ResponseEntity<>(plantHealthCheckImage, HttpStatus.OK);


    }

}
