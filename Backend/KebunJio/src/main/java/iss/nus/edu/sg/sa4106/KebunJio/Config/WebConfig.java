package iss.nus.edu.sg.sa4106.KebunJio.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  
                .allowedOrigins("http://34.124.209.141","http://localhost")  
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") 
                .allowCredentials(true)
                .allowedHeaders("*")  
                .maxAge(3600);  
    }
}
