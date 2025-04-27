package com.ntdev.library.service;

import com.ntdev.library.entity.RedisToken;
import com.ntdev.library.repository.redis.RedisRepository;

import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final RedisRepository redisRepository;

    public RedisService(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    public void save(Long universityId, String token) {
        RedisToken redisToken = new RedisToken(universityId, token);
        redisRepository.save(redisToken);
    }

    public String getToken(Long universityId) {
        RedisToken redisToken = redisRepository.findByUniversityId(universityId);
        return redisToken != null ? redisToken.getToken() : null;
    }

    public void deleteByUniversityId(Long universityId) {
        redisRepository.deleteById(universityId);
    }

    public void deleteByToken(String token) {
        redisRepository.deleteByToken(token);
    }
}
