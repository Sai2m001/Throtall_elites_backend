package com.bca6th.project.motorbikebackend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "bike_views",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_bike_view_per_day",
                columnNames = {"bike_id", "viewer_key", "view_date"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BikeView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bike_id", nullable = false)
    private Long bikeId;

    @Column(name = "viewer_key", nullable = false, length = 50)
    private String viewerKey;

    @Column(name = "view_date", nullable = false)
    private LocalDate viewDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}