package com.ntdev.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration 
public class CorsConfig { 
    @Bean 
    public WebMvcConfigurer corsConfigurer() { 
        return new WebMvcConfigurer() { 
            @Override 
            public void addCorsMappings(CorsRegistry registry) { 
                registry.addMapping("/**") 
                        .allowedOrigins("https://library.thiendev.id.vn", "http://localhost:3000") 
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  
                        .allowedHeaders("Content-Type", "Referrer-Policy") 
                        .allowCredentials(true) 
                        .exposedHeaders("Set-Cookie")
                        .maxAge(3600); 
            }
        };
    }
}


