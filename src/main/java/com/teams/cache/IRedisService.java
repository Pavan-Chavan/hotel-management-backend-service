package com.teams.cache;

import org.springframework.data.redis.connection.RedisClusterNode;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface IRedisService<T> {

    void saveDataIntoRedis(String prefix, String id, T value);

    Boolean saveDataIntoRedisForTtl(String prefix, String id, T value, long timeout, TimeUnit unit);

    void saveListOfKeysDataDataIntoRedis(Map<String,T> map);

    T getDataFromRedis(String prefix, String id);

    List<T> getDataForSetOfKeys(String prefix,Set<String> ids);

    void updateDataIntoRedis(String prefix, String id, T value);

    void deleteDataInRedis(String prefix, String id);

    Long incrementValForKey(String prefix,String id,Long delta);

    Long decrementValForKey(String prefix,String id,Long delta);

    String checkHealthOfRedisNode(RedisClusterNode redisClusterNode);

    Set<String> getListOfKeysInNodeForParticularPattern(RedisClusterNode redisClusterNode,String pattern);

    Boolean isKeyExists(String prefix,String id);//hasKey

    void renameKey(String prefix, String oldId, String newId);
}
