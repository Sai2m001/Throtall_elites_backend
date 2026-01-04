//package com.bca6th.project.motorbikebackend.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.List;
//
//@Bean
//public CorsConfigurationSource corsConfigurationSource() {
//    CorsConfiguration config = new CorsConfiguration();
//    config.setAllowedOrigins(List.of("http://localhost:3000"));
//    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
//    config.setAllowedHeaders(List.of("*"));
//    config.setExposedHeaders(List.of("Authorization"));
//    config.setAllowCredentials(true);
//    config.setMaxAge(3600L);
//
//    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    source.registerCorsConfiguration("/**", config);
//    return source;
//}