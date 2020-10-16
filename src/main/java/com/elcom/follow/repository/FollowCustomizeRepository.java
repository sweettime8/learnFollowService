package com.elcom.follow.repository;

import com.elcom.follow.constant.Constant;
import com.elcom.follow.model.Follow;
import com.elcom.follow.utils.DateUtil;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author anhdv
 */
@Repository
public class FollowCustomizeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(FollowCustomizeRepository.class);

    private SessionFactory sessionFactory;

    @Autowired
    public FollowCustomizeRepository(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }

        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }
    
    public Follow findById(Long id) {
        Session session = openSession();
        try {
            Follow user = session.load(Follow.class, id);
            return user;
        }catch(EntityNotFoundException ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return null;
    }
    
    public boolean unSubscribeObject(String objectType, String objectId, Integer level, String userId) {
        Session session = openSession();
        try {
            Transaction tx = session.beginTransaction();
            String sql = " UPDATE follow SET status = ? WHERE object_type = ? AND object_id = ? AND level = ? AND user_id = ? AND status = ? ";
            Query query = session.createNativeQuery(sql);
            query.setParameter(1, Constant.FOLLOW_STATUS_INACTIVE);
            query.setParameter(2, objectType);
            query.setParameter(3, objectId);
            query.setParameter(4, level);
            query.setParameter(5, userId);
            query.setParameter(6, Constant.FOLLOW_STATUS_ACTIVE);
            boolean updateStatus = query.executeUpdate() >= 1;
            tx.commit();
            return updateStatus;
        }catch(NoResultException ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return false;
    }
    
    public boolean reSubscribeObject(String objectType, String objectId, Integer level, String userId) {
        Session session = openSession();
        try {
            Transaction tx = session.beginTransaction();
            String sql = " UPDATE follow SET status = ? WHERE object_type = ? AND object_id = ? AND level = ? AND user_id = ? AND status = ? ";
            Query query = session.createNativeQuery(sql);
            query.setParameter(1, Constant.FOLLOW_STATUS_ACTIVE);
            query.setParameter(2, objectType);
            query.setParameter(3, objectId);
            query.setParameter(4, level);
            query.setParameter(5, userId);
            query.setParameter(6, Constant.FOLLOW_STATUS_INACTIVE);
            boolean updateStatus = query.executeUpdate() >= 1;
            tx.commit();
            return updateStatus;
        }catch(NoResultException ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return false;
    }
    
    public Integer existsFollow(Follow follow) {
        Session session = openSession();
        try {
            String sql = " SELECT status FROM follow WHERE object_id = ? AND level = ? AND user_id = ? ";
            Object result = session.createNativeQuery(sql)
                            .setParameter(1, follow.getObjectId())
                            .setParameter(2, follow.getLevel())
                            .setParameter(3, follow.getUserId())
                            .uniqueResult();
            return result != null ? (Integer) result : null;
        }catch(Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return null;
    }
    
    public List<String> findByObjectTypeAndObjectIdAndLevel(String objectType, String objectId, Integer level) {
        Session session = openSession();
        try {
            String partition = DateUtil.getMonthAndYearForSelectPartition(new Date());
            String sql = " SELECT user_id FROM follow PARTITION(" + partition + ") WHERE object_type = ? AND object_id = ? AND level = ? AND status = ? ";
            return session.createNativeQuery(sql)
                            .setParameter(1, objectType)
                            .setParameter(2, objectId)
                            .setParameter(3, level)
                            .setParameter(4, Constant.FOLLOW_STATUS_ACTIVE)
                            .getResultList();
        }catch(Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return null;
    }
    
    public void addPartitionsByMonth(String[] tables) {
        Session session = openSession();
        try {
            String partitionName = DateUtil.getPartitionNameOfNextMonth();
            String partitionValue = DateUtil.getPartitionValueOfNextMonth();
            
            Transaction tx = session.beginTransaction();
            String strSql;
            Query query;
            for( String table : tables ) {
                strSql = " ALTER TABLE " + table + " ADD PARTITION (PARTITION " + partitionName + " VALUES LESS THAN (UNIX_TIMESTAMP(?))) ";
                query = session.createNativeQuery(strSql);
                query.setParameter(1, partitionValue);
                query.executeUpdate();
                
//                strSql = " DELETE FROM " + table + " WHERE measure_at < NOW() - INTERVAL ? DAY ";
//                query = session.createNativeQuery(strSql);
//                query.setParameter(1, Constant.DELETE_SCORE_LOG_DATA_AFTER_DAY);
//                query.executeUpdate();
            }
            tx.commit();
        }catch(Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
    }
    
    private Session openSession() {
        Session session = this.sessionFactory.openSession();
        return session;
    }

    private void closeSession(Session session) {
        if (session != null && session.isOpen()) {
            session.disconnect();
            session.close();
        }
    }
}
