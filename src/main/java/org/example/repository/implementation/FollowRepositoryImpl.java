package org.example.repository.implementation;

import org.example.entity.socials.Follow;
import org.example.entity.users.User;
import org.example.repository.FollowRepository;
import org.example.util.JpaOperationUtil;
import org.jboss.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

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

    @Override
    public long countFollowByFollowedId(Long userId) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {
            TypedQuery<Long> query = em.createQuery(
                    "select count(f) from Follow f where f.followedId = :userId", Long.class);

            query.setParameter("userId", userId);

            return query.getSingleResult();
        });
    }

    @Override
    public long countFollowByFollowerId(Long userId) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {
            TypedQuery<Long> query = em.createQuery(
                    "select COUNT(f) from Follow f where f.followerId = :userId", Long.class);

            query.setParameter("userId", userId);

            return query.getSingleResult();
        });
    }

    @Override
    public List<User> getFollowersPaginatedByUserIdOrderByCreateDateAsc(Long userId, Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;

            TypedQuery<User> query = em.createQuery("select u " +
                    "from User u " +
                    "inner join Follow f on f.followerId = u.id " +
                    "where f.followedId = :userId " +
                    "order by f.createDate asc", User.class);

            query.setParameter("userId", userId);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<User> getFollowersPaginatedByUserIdOrderByCreateDateDesc(Long userId, Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;

            TypedQuery<User> query = em.createQuery("select u " +
                    "from User u " +
                    "inner join Follow f on f.followerId = u.id " +
                    "where f.followedId = :userId " +
                    "order by f.createDate desc", User.class);

            query.setParameter("userId", userId);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<User> getFollowingsPaginatedByUserIdOrderByCreateDateAsc(Long userId, Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;

            TypedQuery<User> query = em.createQuery("select u " +
                    "from User u " +
                    "inner join Follow f on f.followedId = u.id " +
                    "where f.followerId = :userId " +
                    "order by f.createDate asc", User.class);

            query.setParameter("userId", userId);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<User> getFollowingsPaginatedByUserIdOrderByCreateDateDesc(Long userId, Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;

            TypedQuery<User> query = em.createQuery("select u " +
                    "from User u " +
                    "inner join Follow f on f.followedId = u.id " +
                    "where f.followerId = :userId " +
                    "order by f.createDate desc", User.class);

            query.setParameter("userId", userId);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }
}
