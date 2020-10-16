package com.elcom.follow.service;

import com.elcom.follow.model.Follow;
import java.util.List;

/**
 *
 * @author anhdv
 */
public interface FollowService {

    void addPartitionsByMonth(String[] tables);
    
    void save(Follow follow);
    
    void saveAll(List<Follow> follow);
    
    Integer existsFollow(Follow follow);
    
    boolean unSubscribeObject(String objectType, String objectId, Integer level, String userId);
    
    boolean reSubscribeObject(String objectType, String objectId, Integer level, String userId);
    
    List<String> findLstFollow(String objectType, String objectId, Integer level);
    
    boolean findFollowStatus(String objectId, Integer level, String userId);
}