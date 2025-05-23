package com.ntdev.library.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ntdev.library.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUniversityId(Long universityId);

    Optional<User> findByEmail(String email);

    Optional<User> findByUniversityIdOrEmail(Long universityId, String email);

    Optional<User> findById(Long id);
}
