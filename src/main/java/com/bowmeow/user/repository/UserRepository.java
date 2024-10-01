package com.bowmeow.user.repository;

import com.bowmeow.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity findByEmail(String email);
    boolean existsByEmail(String email);

    boolean saveVerificationToken( String email, String token );
}
