package com.bca6th.project.motorbikebackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String brand;

    @Column(nullable = false, length = 100)
    private String type;

    @Column(name = "dimension_mm_l_w_h", length = 100)  // was 50
    private String dimensionMmLWH;

    @Column(name = "engine_capacity_cc", nullable = false)
    private Integer engineCapacityCc;

    @Column(name = "engine_type", length = 150)  // was 50 → increased
    private String engineType;

    @Column(name = "max_power", length = 100)  // was 30
    private String maxPower;

    @Column(name = "max_torque", length = 100)  // was 30
    private String maxTorque;

    @Column(name = "mileage_kmpl", length = 50)  // safe
    private String mileageKmpl;

    @Column(name = "top_speed_kmph", length = 50)
    private String topSpeedKmph;

    @Column(name = "gearbox", length = 50)
    private String gearbox;

    @Column(name = "clutch_type", length = 100)  // was 50
    private String clutchType;

    @Column(name = "front_brake", length = 100)  // was 50
    private String frontBrake;

    @Column(name = "rear_brake", length = 100)  // was 50
    private String rearBrake;

    @Column(name = "front_suspension", length = 150)  // was 100
    private String frontSuspension;

    @Column(name = "rear_suspension", length = 150)  // was 100
    private String rearSuspension;

    @Column(name = "front_tyre", length = 100)  // was 50
    private String frontTyre;

    @Column(name = "rear_tyre", length = 100)  // was 50
    private String rearTyre;

    @Column(name = "tyre_type", length = 50)
    private String tyreType;

    @Column(name = "fuel_tank_capacity_l", length = 50)
    private String fuelTankCapacityL;

    @Column(name = "seat_height_mm", length = 50)
    private String seatHeightMm;

    @Column(name = "ground_clearance_mm", length = 50)
    private String groundClearanceMm;

    @Column(name = "kerb_weight_kg", length = 50)
    private String kerbWeightKg;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();
}

//Json format to create and update product
//{
//        "rearSuspension": "Linked‑type Monocross Suspension",
//        "frontTyre": "100/80‑17 Tubeless",
//        "maxPower": "18.5 PS @ 10000 rpm",
//        "frontBrake": "Disc Brake (282 mm) with Single‑Channel ABS",
//        "frontSuspension": "Telescopic Fork",
//        "clutchType": "Wet Multiplate Clutch",
//        "brand": "Yamaha",
//        "rearBrake": "Disc Brake (220 mm)",
//        "mileageKmpl": "45 kmpl",
//        "price": 429900.0,
//        "name": "Yamaha MT‑15",
//        "topSpeedKmph": "118 km/h",
//        "seatHeightMm": "810 mm",
//        "maxTorque": "13.9 Nm @ 8500 rpm",
//        "tyreType": "Tubeless",
//        "fuelTankCapacityL": "10 L",
//        "groundClearanceMm": "170 mm",
//        "stock": 10,
//        "dimensionMmLWH": "2020 mm x 800 mm x 1070 mm",
//        "type": "Naked / Streetfighter",
//        "engineType": "Liquid Cooled, Single Cylinder, 4‑Stroke, SOHC, FI",
//        "gearbox": "6 Speed",
//        "engineCapacityCc": 155,
//        "rearTyre": "140/70‑17 Tubeless",
//        "kerbWeightKg": "138 kg"
//        }