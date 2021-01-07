package com.seed.data.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis manager.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 0:09
 */
@Slf4j
@Component
public class RedisHelper {

    /** Baidu API token redis key, the key should be separated by ":" */
    private String keyBaiduApiToken = "SEED:BAIDU:AI:TOKEN";

    private StringRedisTemplate redisTemplate;

    public RedisHelper(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Nullable
    public String getBaiduApiToken() {
        return redisTemplate.opsForValue().get(keyBaiduApiToken);
    }

    public void saveBaiduApiToken(String token, int expireIn) {
        redisTemplate.opsForValue().set(keyBaiduApiToken, token, expireIn, TimeUnit.SECONDS);
    }

}
