package com.elcom.follow.config;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author anhdv
 */
@Component
public class PropertiesConfig {
    
    public static boolean  APP_MASTER;
    public static long     REDIS_KEY_FOLLOW_LST_EXPIRE_TIMES;
    public static TimeUnit REDIS_KEY_FOLLOW_LST_EXPIRE_UNIT;
    
    @Autowired
    public PropertiesConfig(@Value("${app.master}") boolean isAppMaster
                          , @Value("${redisKey.followLst.ExpTimes}") long redisKeyFollowLstExpTimes
                          , @Value("${redisKey.followLst.ExpUnit}") TimeUnit redisKeyFollowLstExpUnit) {
        PropertiesConfig.APP_MASTER = isAppMaster;
        PropertiesConfig.REDIS_KEY_FOLLOW_LST_EXPIRE_TIMES = redisKeyFollowLstExpTimes;
        PropertiesConfig.REDIS_KEY_FOLLOW_LST_EXPIRE_UNIT = redisKeyFollowLstExpUnit;
    }
}
