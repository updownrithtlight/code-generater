package com.billlv.codegenerator.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class RedisUtility {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Save an object to Redis with a TTL (Time to Live).
     *
     * @param key   the key
     * @param value the value
     * @param ttl   the TTL in seconds
     */
    public void save(String key, Object value, long ttl) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttl));
    }

    /**
     * Save an object to Redis without TTL.
     *
     * @param key   the key
     * @param value the value
     */
    public void save(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * Retrieve an object from Redis by key.
     *
     * @param key the key
     * @return the object, or null if not found
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Delete a key from Redis.
     *
     * @param key the key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Save a hash map to Redis.
     *
     * @param key the key
     * @param map the hash map
     */
    public void saveHash(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * Retrieve a hash map from Redis.
     *
     * @param key the key
     * @return the hash map
     */
    public Map<Object, Object> getHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * Delete a field from a hash in Redis.
     *
     * @param key   the key
     * @param field the field to delete
     */
    public void deleteHashField(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    /**
     * Add an item to a Redis list (push to the end).
     *
     * @param key   the key
     * @param value the value
     */
    public void pushToList(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * Pop an item from a Redis list (pop from the start).
     *
     * @param key the key
     * @return the value, or null if the list is empty
     */
    public Object popFromList(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * Retrieve all items from a Redis list.
     *
     * @param key the key
     * @return the list of items
     */
    public List<Object> getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * Add an item to a Redis set.
     *
     * @param key   the key
     * @param value the value
     */
    public void addToSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * Retrieve all items from a Redis set.
     *
     * @param key the key
     * @return the set of items
     */
    public Set<Object> getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * Remove an item from a Redis set.
     *
     * @param key   the key
     * @param value the value to remove
     */
    public void removeFromSet(String key, String value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    /**
     * Add an item to a Redis sorted set with a score.
     *
     * @param key   the key
     * @param value the value
     * @param score the score
     */
    public void addToZSet(String key, String value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * Retrieve items from a Redis sorted set within a score range.
     *
     * @param key      the key
     * @param minScore the minimum score
     * @param maxScore the maximum score
     * @return the set of items
     */
    public Set<Object> getZSet(String key, double minScore, double maxScore) {
        return redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore);
    }

    /**
     * Remove an item from a Redis sorted set.
     *
     * @param key   the key
     * @param value the value to remove
     */
    public void removeFromZSet(String key, String value) {
        redisTemplate.opsForZSet().remove(key, value);
    }
}
