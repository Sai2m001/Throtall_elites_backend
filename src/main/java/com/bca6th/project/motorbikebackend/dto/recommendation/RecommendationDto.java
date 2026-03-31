package com.bca6th.project.motorbikebackend.dto.recommendation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecommendationDto {
    private Long   id;
    private String name;
    private String brand;
    private String type;
    private Double price;
    private Integer engineCapacityCc;
    private String primaryImageUrl;
    private Long   score;
    private String scoreLabel;
}