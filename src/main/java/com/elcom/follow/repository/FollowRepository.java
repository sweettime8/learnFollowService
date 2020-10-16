package com.elcom.follow.repository;

import com.elcom.follow.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.elcom.follow.model.dto.FollowStatus;

/**
 *
 * @author anhdv
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    
    @Query(value=" SELECT status FROM follow WHERE object_id = :objectId AND level = :level AND user_id = :userId "
            , nativeQuery = true)
    FollowStatus findFollowStatus(
            @Param("objectId") String objectId, @Param("level") Integer level, @Param("userId") String userId
    );
}
