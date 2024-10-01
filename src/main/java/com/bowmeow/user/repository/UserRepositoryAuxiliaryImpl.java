package com.bowmeow.user.repository;

import com.bowmeow.user.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

public class UserRepositoryAuxiliaryImpl implements UserRepositoryAuxiliary {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public boolean saveVerificationToken( String email, String token ) {
        try {
            UserEntity user = entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.email = :email", UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();

//            user.setVerificationToken(token);
            entityManager.merge(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
