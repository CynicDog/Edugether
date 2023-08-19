package org.example.repository.implementation;

import org.example.entity.users.User;
import org.example.repository.UserRepository;
import org.example.util.JpaOperationUtil;
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

        return JpaOperationUtil.apply(entityManagerFactory, em -> {
            User found = em.createQuery("select u from User u where u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return found;
        });
    }

    @Override
    public void insertUser(User user) {

        JpaOperationUtil.consume(entityManagerFactory, em -> {
            em.persist(user);
        });
    }
}
