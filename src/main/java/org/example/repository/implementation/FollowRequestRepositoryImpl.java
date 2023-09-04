package org.example.repository.implementation;

import org.example.entity.socials.FollowRequest;
import org.example.projection.CourseProjection;
import org.example.projection.UserProjection;
import org.example.repository.FollowRequestRepository;
import org.example.util.JpaOperationUtil;
import org.example.util.enums.FOLLOW_REQUEST_STATUS;
import org.jboss.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

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

    @Override
    public List<UserProjection> getPendingRequestsByRecipientId(Long recipientId, Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;

            TypedQuery<UserProjection> query = em.createQuery("select new org.example.projection.UserProjection(" +
                    "u.id, " +
                    "u.username, " +
                    "u.type, " +
                    "fr.id, " +
                    "fr.followRequestStatus) " +
                    "from FollowRequest fr inner join User u on u.id = fr.senderId " +
                    "where fr.followRequestStatus = :status and fr.recipientId = :recipientId " +
                    "order by fr.createDate desc", UserProjection.class);

            query.setParameter("recipientId", recipientId);
            query.setParameter("status", FOLLOW_REQUEST_STATUS.PENDING);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<UserProjection> getAcceptedRequestsByRecipientId(Long recipientId, Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;

            TypedQuery<UserProjection> query = em.createQuery("select new org.example.projection.UserProjection(" +
                    "u.id, " +
                    "u.username, " +
                    "u.type, " +
                    "fr.id, " +
                    "fr.followRequestStatus) " +
                    "from FollowRequest fr inner join User u on u.id = fr.senderId " +
                    "where fr.followRequestStatus = :status and fr.recipientId = :recipientId " +
                    "order by fr.createDate desc", UserProjection.class);

            query.setParameter("recipientId", recipientId);
            query.setParameter("status", FOLLOW_REQUEST_STATUS.ACCEPTED);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<UserProjection> getDeclinedRequestsByRecipientId(Long recipientId, Integer page, Integer limit) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {

            int startIndex = page * limit;

            TypedQuery<UserProjection> query = em.createQuery("select new org.example.projection.UserProjection(" +
                    "u.id, " +
                    "u.username, " +
                    "u.type, " +
                    "fr.id, " +
                    "fr.followRequestStatus) " +
                    "from FollowRequest fr inner join User u on u.id = fr.senderId " +
                    "where fr.followRequestStatus = :status and fr.recipientId = :recipientId " +
                    "order by fr.createDate desc", UserProjection.class);

            query.setParameter("recipientId", recipientId);
            query.setParameter("status", FOLLOW_REQUEST_STATUS.DECLINED);

            query.setFirstResult(startIndex);
            query.setMaxResults(limit);

            return query.getResultList();
        });
    }

    @Override
    public List<UserProjection> getSentRequestsByRecipientId(Long recipientId, Integer page, Integer limit) {
        return null;
    }

    @Override
    public FollowRequest getRequestById(Long requestId) {
        return JpaOperationUtil.apply(entityManagerFactory, em -> {
            return em.find(FollowRequest.class, requestId);
        });
    }

    @Override
    public void updateFollowRequest(FollowRequest followRequest) {
        JpaOperationUtil.consume(entityManagerFactory, em -> {
            try {
                em.merge(followRequest);
            } catch (Exception e) {
                throw e;
            }
        });
    }
}
