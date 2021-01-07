package com.seed.base.utils;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 12:15
 */
public final class RedisUtils {

    public static long getCountForBitmap(StringRedisTemplate redisTemplate, String key) {
        Long count = redisTemplate.execute((RedisCallback<Long>) connection -> connection.bitCount(key.getBytes()));
        return count == null ? 0 : count;
    }

    public static void putHash(StringRedisTemplate redisTemplate, String key, String hashKey, long value) {
        redisTemplate.opsForHash().put(key, hashKey, String.valueOf(value));
    }

    public static void putHash(StringRedisTemplate redisTemplate, String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public static void putHash(StringRedisTemplate redisTemplate, String key, long hashKey, long value) {
        redisTemplate.opsForHash().put(key, String.valueOf(hashKey), String.valueOf(value));
    }

    public static void putHash(StringRedisTemplate redisTemplate, String key, long hashKey, String value) {
        redisTemplate.opsForHash().put(key, String.valueOf(hashKey), value);
    }

    public static Long getLongHash(StringRedisTemplate redisTemplate, String key, String hashKey) {
        String obj = (String) redisTemplate.opsForHash().get(key, hashKey);
        if (obj == null) {
            return null;
        }
        return Long.parseLong(obj);
    }

    public static Long getLongHash(StringRedisTemplate redisTemplate, String key, long hashKey) {
        String obj = (String) redisTemplate.opsForHash().get(key, String.valueOf(hashKey));
        if (obj == null) {
            return null;
        }
        return Long.parseLong(obj);
    }

    public static List<Long> getLongList(StringRedisTemplate redisTemplate, String key) {
        List<Long> ids = new LinkedList<>();
        List<String> list = redisTemplate.opsForList().range(key, 0, -1);
        if (list != null && !list.isEmpty()) {
            list.stream().map(Long::parseLong).forEach(ids::add);
        }
        return ids;
    }

    public static List<Long> getLongList(StringRedisTemplate redisTemplate, String key, long start, long end) {
        List<Long> ids = new LinkedList<>();
        List<String> list = redisTemplate.opsForList().range(key, start, end);
        if (list != null && !list.isEmpty()) {
            list.stream().map(Long::parseLong).forEach(ids::add);
        }
        return ids;
    }

    public static boolean getBit(StringRedisTemplate redisTemplate, String key, long offset) {
        Boolean b = redisTemplate.opsForValue().getBit(key, offset);
        return b != null && b;
    }

    public static void setBit(StringRedisTemplate redisTemplate, String key, long offset, boolean value) {
        redisTemplate.opsForValue().setBit(key, offset, value);
    }

    public static boolean delete(StringRedisTemplate redisTemplate, String key) {
        Boolean b = redisTemplate.delete(key);
        return b != null && b;
    }

    private RedisUtils(StringRedisTemplate redisTemplate) {
        throw new UnsupportedOperationException("u can't initialize me!");
    }

}
