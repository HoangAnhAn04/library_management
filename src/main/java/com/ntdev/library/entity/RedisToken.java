package com.ntdev.library.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@RedisHash(value = "auth:token", timeToLive = 86400)
public class RedisToken implements Serializable {
    @Id
    private Long universityId;
    private String token;
}
