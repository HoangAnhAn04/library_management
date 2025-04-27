package com.ntdev.library.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "password")
public class PasswordConfig {

    private String secret;
    private String algorithm;
    private int iterations;
    private int keyLength;
}
