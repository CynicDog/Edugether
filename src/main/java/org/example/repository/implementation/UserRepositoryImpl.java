package org.example.repository.implementation;

import org.example.entity.users.User;
import org.example.repository.UserRepository;
import org.jboss.logging.Logger;

import javax.persistence.EntityManagerFactory;

public class UserRepositoryImpl implements UserRepository {

    private final Logger logger = Logger.getLogger(UserRepositoryImpl.class);
    private final EntityManagerFactory entityManagerFactory;

    public UserRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public User loadUserByUsername(String username) {
        return null;
    }
}
