package iss.nus.edu.sg.sa4106.KebunJio.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @fileName: WebConfig
 * @version: V1.0
 * @Description: TODO
 * 代码目的，作用，如何工作
 * @Date: 2025/2/9 15:58
 * 本代码要注意的事项、备注事项等。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3001") // 或使用 allowedOrigins
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}