package com.ntdev.library.repository.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ntdev.library.entity.RedisToken;

@Repository
public interface RedisRepository extends CrudRepository<RedisToken, Long> {
    RedisToken findByUniversityId(Long universityId);

    void deleteByToken(String token);
}
