package com.bca6th.project.motorbikebackend.repository;

import com.bca6th.project.motorbikebackend.model.BikeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BikeViewRepository extends JpaRepository<BikeView, Long> {

    boolean existsByBikeIdAndViewerKeyAndViewDate(Long bikeId, String viewerKey, LocalDate viewDate);

    @Query("""
        SELECT v.bikeId, COUNT(v) AS viewCount
        FROM BikeView v
        WHERE v.viewDate >= :since
        GROUP BY v.bikeId
        ORDER BY viewCount DESC
    """)
    List<Object[]> findTopViewedBikeIdsSince(
            @Param("since") LocalDate since,
            org.springframework.data.domain.Pageable pageable
    );
}