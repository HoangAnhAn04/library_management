package com.ntdev.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.ntdev.library.repository.jpa")
@EnableRedisRepositories(basePackages = "com.ntdev.library.repository.redis")
public class RepositoryConfig {
}
