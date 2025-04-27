package com.ntdev.library.config;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String secret;
    private String cookieName;
    private long expirationMs;
    private String domain;
}
