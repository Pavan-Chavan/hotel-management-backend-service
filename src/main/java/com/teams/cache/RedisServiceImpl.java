package com.teams.cache;


import io.lettuce.core.RedisCommandTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RedisServiceImpl<T> implements IRedisService<T>{

    @Autowired
    RedisTemplate<String,T> redisTemplate;

    @Value("${redis.ttl.days:7}")
    private Long ttlDays;

    private String getKey(String prefix, String id) {
        if(StringUtils.isNotEmpty(prefix) && StringUtils.isNotEmpty(id)) {
            return prefix + ":" + id;
        } else {
            throw new IllegalArgumentException("Invalid data provided prefix or id is empty or null");
        }
    }

    @Override
    public Boolean isKeyExists(String prefix,String id) {
        try{
            String key = getKey(prefix,id);
            log.info("Checking in redis whether is key {} is available or not ",key);
            Boolean isAvailable = redisTemplate.hasKey(key);
            log.info("Key {} is available in redis: {}",key,isAvailable);
            return isAvailable;
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failed", ex);
            throw ex;
        } catch (RedisCommandTimeoutException ex) {
            log.error("Redis operation timed out", ex);
            throw ex;
        } catch (SerializationException ex) {
            log.error("Serialization error", ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("Invalid input provided, data might be empty or null", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("An error occurred while performing Redis operation", ex);
            throw ex;
        }
    }

    @Override
    public void renameKey(String prefix, String oldId, String newId) {
        try{
            String oldKey = getKey(prefix,oldId);
            String newKey = getKey(prefix,newId);
            log.info("Renaming the oldKey {} to newKey {}",oldKey,newKey);
            Boolean isRenamed = redisTemplate.renameIfAbsent(oldKey,newKey);
            log.info("oldKey {} is renamed to newKey {} in redis successfully {}",oldKey,newKey,isRenamed);
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failed", ex);
            throw ex;
        } catch (RedisCommandTimeoutException ex) {
            log.error("Redis operation timed out", ex);
            throw ex;
        } catch (SerializationException ex) {
            log.error("Serialization error", ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("Invalid input provided, data might be empty or null", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("An error occurred while performing Redis operation", ex);
            throw ex;
        }
    }

    @Override
    public void saveDataIntoRedis(String prefix, String id, T value) {
        try{
            String key = getKey(prefix,id);
            log.info("Saving the data for key: {} ",key);
            redisTemplate.opsForValue().set(key,value);
            log.info("Data saved successfully for key: {}",key);
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failed", ex);
            throw ex;
        } catch (RedisCommandTimeoutException ex) {
            log.error("Redis operation timed out", ex);
            throw ex;
        } catch (SerializationException ex) {
            log.error("Serialization error", ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("Invalid input provided, data might be empty or null", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("An error occurred while performing Redis operation", ex);
            throw ex;
        }
    }

    @Override
    public Boolean saveDataIntoRedisForTtl(String prefix, String id, T value, long timeout, TimeUnit unit) {
        try{
            String key = getKey(prefix,id);
            log.info("Retrieving the data for key: {} ",key);
            Boolean success = redisTemplate.opsForValue().setIfPresent(key,value,ttlDays,TimeUnit.DAYS);
            log.info("Data saved successfully for key: {}",key);
            return success;
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failed", ex);
            throw ex;
        } catch (RedisCommandTimeoutException ex) {
            log.error("Redis operation timed out", ex);
            throw ex;
        } catch (SerializationException ex) {
            log.error("Serialization error", ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("Invalid input provided, data might be empty or null", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("An error occurred while performing Redis operation", ex);
            throw ex;
        }
    }

    @Override
    public void saveListOfKeysDataDataIntoRedis(Map<String, T> map) {
        try{
            if(MapUtils.isNotEmpty(map)) {
                log.info("Retrieving the data for keySize: {} ",map.size());
                redisTemplate.opsForValue().multiSet(map);
                log.info("Data saved successfully for map of keys: {}",map);
            } else{
                throw new IllegalArgumentException("Invalid data provided");
            }

        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failed", ex);
            throw ex;
        } catch (RedisCommandTimeoutException ex) {
            log.error("Redis operation timed out", ex);
            throw ex;
        } catch (SerializationException ex) {
            log.error("Serialization error", ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("Invalid input provided, data might be empty or null", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("An error occurred while performing Redis operation", ex);
            throw ex;
        }
    }

    @Override
    public T getDataFromRedis(String prefix, String id) {
        try{
            String key = getKey(prefix,id);
            log.info("Retrieving the data for key: {} ",key);
            T value = redisTemplate.opsForValue().get(key);
            log.info("Data retrieved for key: {} successfully",key);
            return value;
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failed", ex);
            throw ex;
        } catch (RedisCommandTimeoutException ex) {
            log.error("Redis operation timed out", ex);
            throw ex;
        } catch (SerializationException ex) {
            log.error("Serialization error", ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("Invalid input provided, data might be empty or null", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("An error occurred while performing Redis operation", ex);
            throw ex;
        }
    }

    @Override
    public List<T> getDataForSetOfKeys(String prefix,Set<String> ids) {
        try{
            BiFunction<String, String, String> keyGenerator = this::getKey;
            Set<String> keyList = ids.stream().map(id->keyGenerator.apply(prefix,id)).collect(Collectors.toSet());

            log.info("Retrieving the data for keys: {} ",keyList);
            List<T> objectList= redisTemplate.opsForValue().multiGet(keyList);
            log.info("Data retrieved for keys: {} successfully",keyList);
            return objectList;
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failed", ex);
            throw ex;
        } catch (RedisCommandTimeoutException ex) {
            log.error("Redis operation timed out", ex);
            throw ex;
        } catch (SerializationException ex) {
            log.error("Serialization error", ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("Invalid input provided, data might be empty or null", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("An error occurred while performing Redis operation", ex);
            throw ex;
        }
    }

    @Override
    public void updateDataIntoRedis(String prefix, String id, T value) {

    }

    @Override
    public void deleteDataInRedis(String prefix, String id) {

    }

    @Override
    public Long incrementValForKey(String prefix, String id, Long delta) {
        try{
            String key = getKey(prefix,id);
            log.info("Incrementing the data for key: {} by {}",key,delta);
            Long value= redisTemplate.opsForValue().increment(key,delta);
            log.info("Increment the value for key: {} successfully",key);
            return value;
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failed", ex);
            throw ex;
        } catch (RedisCommandTimeoutException ex) {
            log.error("Redis operation timed out", ex);
            throw ex;
        } catch (SerializationException ex) {
            log.error("Serialization error", ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("Invalid input provided, data might be empty or null", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("An error occurred while performing Redis operation", ex);
            throw ex;
        }
    }

    @Override
    public Long decrementValForKey(String prefix,String id, Long delta) {
        try{
            String key = getKey(prefix,id);
            log.info("Decrementing the data for key: {} by {}",key,delta);
            Long value= redisTemplate.opsForValue().decrement(key,delta);
            log.info("Decrementing the value for key: {} successfully",key);
            return value;
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failed", ex);
            throw ex;
        } catch (RedisCommandTimeoutException ex) {
            log.error("Redis operation timed out", ex);
            throw ex;
        } catch (SerializationException ex) {
            log.error("Serialization error", ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("Invalid input provided, data might be empty or null", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("An error occurred while performing Redis operation", ex);
            throw ex;
        }
    }

    @Override
    public String checkHealthOfRedisNode(RedisClusterNode redisClusterNode) {
        try{
            if(StringUtils.isEmpty(redisClusterNode.getHost())) {
                throw new IllegalArgumentException("Invalid data provided host is empty or null");
            }

            log.info("Checking the health node {}",redisClusterNode.getHost());
            return redisTemplate.opsForCluster().ping(redisClusterNode);
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failed", ex);
            throw ex;
        } catch (RedisCommandTimeoutException ex) {
            log.error("Redis operation timed out", ex);
            throw ex;
        } catch (SerializationException ex) {
            log.error("Serialization error", ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("Invalid input provided, data might be empty or null", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("An error occurred while performing Redis operation", ex);
            throw ex;
        }
    }

    @Override
    public Set<String> getListOfKeysInNodeForParticularPattern(RedisClusterNode redisClusterNode,String pattern) {
        try{
            if(StringUtils.isEmpty(redisClusterNode.getHost()) || StringUtils.isEmpty(pattern)) {
                throw new IllegalArgumentException("Invalid data provided host or pattern is empty or null");
            }
            log.info("Retrieving the list of keys in cluster {} for pattern {}",redisClusterNode.getHost(),pattern);
            return redisTemplate.opsForCluster().keys(redisClusterNode,pattern);
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failed", ex);
            throw ex;
        } catch (RedisCommandTimeoutException ex) {
            log.error("Redis operation timed out", ex);
            throw ex;
        } catch (SerializationException ex) {
            log.error("Serialization error", ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("Invalid input provided, data might be empty or null", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("An error occurred while performing Redis operation", ex);
            throw ex;
        }
    }

}
