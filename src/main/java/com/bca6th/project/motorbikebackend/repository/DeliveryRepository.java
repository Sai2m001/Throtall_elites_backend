package com.bca6th.project.motorbikebackend.repository;

import com.bca6th.project.motorbikebackend.model.Delivery;
import com.bca6th.project.motorbikebackend.model.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findAllByOrderByCreatedAtDesc();
    List<Delivery> findByStatus(DeliveryStatus status);
    List<Delivery> findByCustomerEmailIgnoreCase(String email);
}