package com.example.tasteofkorea.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173",
                        "https://taste-of-korea-fe.vercel.app")  // React 앱의 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}

