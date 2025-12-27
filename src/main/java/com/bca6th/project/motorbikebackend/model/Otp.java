package com.bca6th.project.motorbikebackend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@RedisHash("otp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Otp implements Serializable {
    @Id
    private String email;

    private String code;

    @TimeToLive
    private Long ttl = 300L; //5 min = 300L
}
