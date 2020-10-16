/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.follow.redis;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class RedisPaging {
    
    private final Logger LOGGER = LoggerFactory.getLogger(RedisPaging.class);
    
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Store a single hash cache
     *
     * @param key key
     * @param hkey key
     * @param value value
     * @return
     */
    public boolean hput(String key, String hkey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hkey, value);
            LOGGER.debug("hput {} = {}", key + hkey, value);
            return true;
        } catch (Exception e) {
            LOGGER.warn("hput {} = {}", key + hkey, value, e);
        }
        return false;
    }

    /**
     * Paging access data
     *
     * @param key hash Access key
     * @param hkey hash Accessed hkey
     * @param createdAt Specify field sorting
     * @param value
     * @return
     */
    public boolean setPage(String key, String hkey, Long createdAt, String value) {
        boolean result = false;
        try {
            redisTemplate.opsForZSet().add(key + ":page", hkey, createdAt);
            result = hput(key, hkey, value);
            LOGGER.debug("setPage {}", key);
        } catch (Exception e) {
            LOGGER.warn("setPage {}", key, e);
        }
        return result;
    }

    /**
     * Paging out the hkey value in the hash
     *
     * @param key
     * @param offset
     * @param count
     * @return
     */
    public Set<Object> getPage(String key, int offset, int count) {
        Set<Object> result = null;
        try {
            result = redisTemplate.opsForZSet().rangeByScore(key + ":page", 1, 100000, 
                    (offset - 1) * count, count);
            //100000 represents the sort atmosphere value of score, i.e. the range from 1-100000 
            LOGGER.debug("getPage {}", key);
        } catch (Exception e) {
            LOGGER.warn("getPage {}", key, e);
        }
        return result;
    }

    /**
     * Calculate the quantity corresponding to the key value
     *
     * @param key
     * @return
     */
    public Integer getSize(String key) {
        Integer num = 0;
        try {
            Long size = redisTemplate.opsForZSet().zCard(key + ":page");
            LOGGER.debug("getSize {}", key);
            return size.intValue();
        } catch (Exception e) {
            LOGGER.warn("getSize {}", key, e);
        }
        return num;
    }
}
