package com.bowmeow.user.repository;

public interface UserRepositoryAuxiliary {
    boolean saveVerificationToken(String email, String token);
}
