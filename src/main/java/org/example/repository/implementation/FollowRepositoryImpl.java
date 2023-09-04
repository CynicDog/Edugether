package org.example.repository.implementation;

import org.example.entity.socials.Follow;
import org.example.repository.FollowRepository;
import org.example.util.JpaOperationUtil;
import org.jboss.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class FollowRepositoryImpl implements FollowRepository {

    private final Logger logger = Logger.getLogger(FollowRepositoryImpl.class);
    private final EntityManagerFactory entityManagerFactory;

    public FollowRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void insertFollow(Follow follow) {
        JpaOperationUtil.consume(entityManagerFactory, em -> {
            try {
                em.persist(follow);
            } catch (Exception e) {
                throw e;
            }
        });
    }

    @Override
    public void deleteFollow(Follow follow) {
        JpaOperationUtil.consume(entityManagerFactory, em -> {
            try {
                if (follow != null) {

                    // to take control on the entity that is to be deleted
                    Follow managedFollow = em.merge(follow);
                    em.remove(managedFollow);
                }
            } catch (Exception e) {
                throw e;
            }
        });
    }

    @Override
    public Follow getFollowByFollowerAndFollowed(Long senderId, Long recipientId) {

        return JpaOperationUtil.apply(entityManagerFactory, em -> {
            try {
                TypedQuery<Follow> query = em.createQuery("select f from Follow f where f.followerId = :followerId and f.followedId = :followedId", Follow.class);

                query.setParameter("followerId", senderId);
                query.setParameter("followedId", recipientId);

                return query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        });
    }
}
