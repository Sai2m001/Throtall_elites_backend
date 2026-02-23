package com.bca6th.project.motorbikebackend.repository;

import com.bca6th.project.motorbikebackend.model.TestRideRequest;
import com.bca6th.project.motorbikebackend.model.TestRideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRideRepository extends JpaRepository<TestRideRequest, Long> {
    List<TestRideRequest> findAllByOrderByCreatedAtDesc();
    List<TestRideRequest> findByStatus(TestRideStatus status);
    List<TestRideRequest> findByUserId(Long userId);
}