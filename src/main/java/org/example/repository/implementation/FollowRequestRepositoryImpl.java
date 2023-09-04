package org.example.repository.implementation;

import org.example.entity.socials.FollowRequest;
import org.example.repository.FollowRequestRepository;
import org.example.util.JpaOperationUtil;
import org.jboss.logging.Logger;

import javax.persistence.EntityManagerFactory;

public class FollowRequestRepositoryImpl implements FollowRequestRepository {

    private final Logger logger = Logger.getLogger(FollowRequestRepositoryImpl.class);
    private final EntityManagerFactory entityManagerFactory;

    public FollowRequestRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void insertFollowRequest(FollowRequest followRequest) {

        try {
            JpaOperationUtil.consume(entityManagerFactory, em -> {
                em.persist(followRequest);
            });
        } catch (Exception e) {
            logger.info("[ FollowRequestRepositoryImpl.insertFollowRequest(FollowRequest followRequest) ]: " + e.getMessage());
            throw e;
        }
    }
}
