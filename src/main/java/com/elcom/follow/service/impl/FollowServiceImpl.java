package com.elcom.follow.service.impl;

import com.elcom.follow.config.PropertiesConfig;
import com.elcom.follow.constant.Constant;
import com.elcom.follow.model.Follow;
import com.elcom.follow.repository.FollowCustomizeRepository;
import com.elcom.follow.repository.FollowRepository;
import com.elcom.follow.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import com.elcom.follow.model.dto.FollowStatus;

/**
 *
 * @author anhdv
 */
@Service
public class FollowServiceImpl implements FollowService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FollowServiceImpl.class);
    
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private FollowCustomizeRepository followCustomizeRepository;
    
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void addPartitionsByMonth(String[] tables) {
        this.followCustomizeRepository.addPartitionsByMonth(tables);
    }
    
    @Override
    public void save(Follow follow) {
        this.followRepository.save(follow);
        
        // Nếu lưu thành công thì reset redis getList
        if( follow.getId()!=null ) {
            String lstFollowkey = Constant.FOLLOW_CACHE_LST_BY_PARAMS + "_" + follow.getObjectType()+ "_" + follow.getObjectId()+ "_" + follow.getLevel();
            if( redisTemplate.hasKey(lstFollowkey) )
                redisTemplate.delete(lstFollowkey);
        }
    }
    
    @Override
    public void saveAll(List<Follow> followLst) {
        this.followRepository.saveAll(followLst);
        
        // Nếu lưu thành công thì reset redis getList
        for( Follow follow : followLst ) {
            if( follow.getId()!=null ) {
                String lstFollowkey = Constant.FOLLOW_CACHE_LST_BY_PARAMS + "_" + follow.getObjectType()+ "_" + follow.getObjectId()+ "_" + follow.getLevel();
                if( redisTemplate.hasKey(lstFollowkey) )
                    redisTemplate.delete(lstFollowkey);
            }
        }
    }
    
    @Override
    public Integer existsFollow(Follow follow) {
        return this.followCustomizeRepository.existsFollow(follow);
    }
    
    @Override
    public boolean reSubscribeObject(String objectType, String objectId, Integer level, String userId) {
        boolean updateStatus = this.followCustomizeRepository.reSubscribeObject(objectType, objectId, level, userId);
        
        // Nếu lưu thành công thì reset redis getList và redis getStatus
        if( updateStatus ) {
            String lstFollowkey = Constant.FOLLOW_CACHE_LST_BY_PARAMS + "_" + objectType + "_" + objectId + "_" + level;
            if( redisTemplate.hasKey(lstFollowkey) )
                redisTemplate.delete(lstFollowkey);
            
            String followStatusKey = Constant.FOLLOW_CACHE_GET_STATUS + "_" + objectId+ "_" + level + "_" + userId;
            if( redisTemplate.hasKey(followStatusKey) )
                redisTemplate.delete(followStatusKey);
        }
        return updateStatus;
    }
    
    @Override
    public boolean unSubscribeObject(String objectType, String objectId, Integer level, String userId) {
        boolean updateStatus = this.followCustomizeRepository.unSubscribeObject(objectType, objectId, level, userId);
        
        // Nếu lưu thành công thì reset redis getList và redis getStatus
        if( updateStatus ) {
            String lstFollowkey = Constant.FOLLOW_CACHE_LST_BY_PARAMS + "_" + objectType + "_" + objectId + "_" + level;
            if( redisTemplate.hasKey(lstFollowkey) )
                redisTemplate.delete(lstFollowkey);
            
            String followStatusKey = Constant.FOLLOW_CACHE_GET_STATUS + "_" + objectId+ "_" + level + "_" + userId;
            if( redisTemplate.hasKey(followStatusKey) )
                redisTemplate.delete(followStatusKey);
        }
        return updateStatus;
    }
    
    @Override
    public List<String> findLstFollow(String objectType, String objectId, Integer level) {
        
        String lstFollowkey = Constant.FOLLOW_CACHE_LST_BY_PARAMS + "_" + objectType+ "_" + objectId+ "_" + level;
        List<String> followLst;
        
        if( redisTemplate.hasKey(lstFollowkey) )
            followLst = (List<String>) redisTemplate.opsForList().range(lstFollowkey, 0, -1);
        else {
            followLst = this.followCustomizeRepository.findByObjectTypeAndObjectIdAndLevel(objectType, objectId, level);
            if( followLst!=null && !followLst.isEmpty() ) {
                Long pushValStatus = redisTemplate.opsForList().rightPushAll(lstFollowkey, followLst);
                
                // Nếu push thành công thì set hết hạn key
                if( pushValStatus!=null && !pushValStatus.equals(0L) )
                    redisTemplate.expire(lstFollowkey, PropertiesConfig.REDIS_KEY_FOLLOW_LST_EXPIRE_TIMES
                                        , PropertiesConfig.REDIS_KEY_FOLLOW_LST_EXPIRE_UNIT);
            }
        }
        
        return followLst;
    }
    
    @Override
    public boolean findFollowStatus(String objectId, Integer level, String userId) {
        
        String lstFollowKey = Constant.FOLLOW_CACHE_GET_STATUS + "_" + objectId+ "_" + level + "_" + userId;
        boolean status = false;
        
        if( redisTemplate.hasKey(lstFollowKey) )
            status = (boolean) redisTemplate.opsForValue().get(lstFollowKey);
        else {
            FollowStatus followStatus = this.followRepository.findFollowStatus(objectId, level, userId);
            if( followStatus!=null && followStatus.getStatus()!=null ) {
                status = followStatus.getStatus().equals(1L);
                redisTemplate.opsForValue().set(lstFollowKey, status);

                // Nếu push thành công thì set hết hạn key
                redisTemplate.expire(lstFollowKey, PropertiesConfig.REDIS_KEY_FOLLOW_LST_EXPIRE_TIMES
                                    , PropertiesConfig.REDIS_KEY_FOLLOW_LST_EXPIRE_UNIT);
            }
        }
        return status;
    }
}
