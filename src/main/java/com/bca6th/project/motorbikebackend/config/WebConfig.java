package com.bca6th.project.motorbikebackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + uploadDir + "/";
        System.out.println("Serving /uploads/** from: " + new java.io.File(uploadDir).getAbsolutePath());

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);
    }
}