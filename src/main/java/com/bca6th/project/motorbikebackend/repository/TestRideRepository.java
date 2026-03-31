package com.bca6th.project.motorbikebackend.repository;

import com.bca6th.project.motorbikebackend.model.TestRideRequest;
import com.bca6th.project.motorbikebackend.model.TestRideStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRideRepository extends JpaRepository<TestRideRequest, Long> {

    List<TestRideRequest> findAllByOrderByCreatedAtDesc();
    List<TestRideRequest> findByStatus(TestRideStatus status);
    List<TestRideRequest> findByUserId(Long userId);

    @Query("""
        SELECT t.bikeId, COUNT(t) AS requestCount
        FROM TestRideRequest t
        GROUP BY t.bikeId
        ORDER BY requestCount DESC
    """)
    List<Object[]> findTopRequestedBikeIds(Pageable pageable);
}