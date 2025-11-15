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
    private String type; // Naked, Cruiser, Sport, etc.

    @Column(name = "engine_capacity_cc", nullable = false)
    private Integer engineCapacityCc;

    @Column(name = "engine_type", nullable = false, length = 50)
    private String engineType; // air-cooled, liquid-cooled, FI

    @Column(name = "max_power", nullable = false, length = 30)
    private String maxPower;  // hp/Ps

    @Column(name = "max_torque", nullable = false, length = 30)
    private String maxTorque;  //Nm

    @Column(name = "mileage_kmpl", length = 20)
    private String mileageKmpl;

    @Column(name = "top_speed_kmph", length = 20)
    private String topSpeedKmph;

    @Column(name = "gearbox", length = 20)
    private String gearbox; // 5-speed, 6-speed

    @Column(name = "clutch_type", length = 50)
    private String clutchType; //slipper clutch, wet multi-plate, etc

    // Brake : disc/drum + ABS
    @Column(name = "front_brake", length = 50)
    private String frontBrake;

    @Column(name = "rear_brake", length = 50)
    private String rearBrake;

    //F.Suspension : telescope, USD fork
    @Column(name = "front_suspension", length = 100)
    private String frontSuspension;

    //R.suspension : monoshock, dual-shock
    @Column(name = "rear_suspension", length = 100)
    private String rearSuspension;

    @Column(name = "front_tyre", length = 50)
    private String frontTyre;

    @Column(name = "rear_tyre", length = 50)
    private String rearTyre;

    @Column(name = "tyre_type", length = 30)
    private String tyreType;

    @Column(name = "fuel_tank_capacity_l", length = 20)
    private String fuelTankCapacityL;

    @Column(name = "seat_height_mm", length = 20)
    private String seatHeightMm;

    @Column(name = "ground_clearance_mm", length = 20)
    private String groundClearanceMm;

    @Column(name = "kerb_weight_kg", length = 20)
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


//JSON format example:
//{
//        "name": "Bajaj Pulsar NS200",
//        "brand": "Bajaj",
//        "type": "Naked",
//        "engineCapacityCc": 200,
//        "engineType": "Liquid Cooled, Single Cylinder, 4-Stroke, FI",
//        "maxPower": "24.5 PS @ 9750 rpm",
//        "maxTorque": "18.6 Nm @ 8000 rpm",
//        "mileageKmpl": "35 km/l",
//        "topSpeedKmph": "136 km/h",
//        "gearbox": "6 Speed",
//        "clutchType": "Wet Multi-plate",
//        "frontBrake": "300mm Disc, Dual Channel ABS",
//        "rearBrake": "230mm Disc, Single Channel ABS",
//        "frontSuspension": "Telescopic with Anti-friction Bush",
//        "rearSuspension": "Nitrox Mono Shock Absorber",
//        "frontTyre": "100/80-17 52P Tubeless",
//        "rearTyre": "130/70-17 62P Tubeless",
//        "tyreType": "Tubeless",
//        "fuelTankCapacityL": "12 L",
//        "seatHeightMm": "805 mm",
//        "groundClearanceMm": "168 mm",
//        "kerbWeightKg": "158 kg",
//        "price": 135000.0,
//        "stock": 25,
//        "active": true,
//        "images": [
//        {
//        "imageUrl": "https://example.com/pulsar-ns200-front.jpg",
//        "primary": true
//        },
//        {
//        "imageUrl": "https://example.com/pulsar-ns200-side.jpg",
//        "primary": false
//        },
//        {
//        "imageUrl": "https://example.com/pulsar-ns200-rear.jpg",
//        "primary": false
//        }
//        ]
//        }
