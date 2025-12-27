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
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        //Serve files under /uploads/** URL pattern from the file system location specified by uploadDir
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:"+ uploadDir + "/");
        // Optional: Also serve from classpath (for default images if any)
        // registry.addResourceHandler("/uploads/**")
        //         .addResourceLocations("classpath:/uploads/");
    }
}
