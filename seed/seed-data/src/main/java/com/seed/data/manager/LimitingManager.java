package com.seed.data.manager;

import cn.hutool.core.util.StrUtil;
import com.seed.base.annotation.Limiting;
import com.seed.base.utils.RedisUtils;
import com.seed.data.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 12:13
 */
@Slf4j
@Component
public class LimitingManager {

    private StringRedisTemplate redisTemplate;

    public LimitingManager(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Normal request limiting. Check ip to url count and limit ip.
     *
     * @param ip  the ip
     * @param url the url requested
     * @return    pass limit or nor, true pass, otherwise not
     */
    public boolean increaseNormalVisit(String ip, String url) {
        if (isIpInWhiteList(ip)) return true;
        // Judge if given ip was limited and judge the limit time
        Long time = RedisUtils.getLongHash(redisTemplate, "SEED:LIMITING:LIMITED", ip);
        if (time != null && (time + Constants.NORMAL_REQUEST_LIMIT_IN_SECONDS)*1000 >= System.currentTimeMillis()) return false;
        // Get current visit count and judge limiting
        String key = String.format("SEED:LIMITING:API:NORMAL:%s:TO:%s", ip, url);
        Long count = redisTemplate.opsForList().size(key);
        if (count != null && count >= Constants.NORMAL_MAX_REQUEST_COUNT_IN_SECOND) return false;
        // Increase visit count and set expire time, here we use list to count visit times
        redisTemplate.opsForList().rightPush(key, "0");
        if (count == null || count == 0) redisTemplate.expire(key, 1, TimeUnit.SECONDS);
        return true;
    }

    /**
     * The request limiting by {@link Limiting} annotation. Check the annotation information and
     * if the given ip hit the limiting.
     *
     * @param ip       the ip
     * @param url      the url
     * @param limiting the annotation
     * @return         true to pass else not
     */
    public boolean increaseIpToUrlVisitCount(String ip, String url, Limiting limiting) {
        if (isIpInWhiteList(ip)) return true;
        String key = String.format("SEED:LIMITING:API:CONFIG:%s:TO:%s", ip, url);
        Long count = redisTemplate.opsForList().size(key);
        if (count != null && count >= limiting.limit()) return false;
        redisTemplate.opsForList().rightPush(key, "0");
        if (count == null || count == 0) redisTemplate.expire(key, limiting.timeout(), limiting.timeUnit());
        return true;
    }

    /** Is given ip in white list */
    private boolean isIpInWhiteList(String ip) {
        /* Ip white list, no limiting */
        String keyIpWhiteList = "SEED:LIMITING:IP:WHITE";
        List<String> list = redisTemplate.opsForList().range(keyIpWhiteList, 0, -1);
        if (list != null && !list.isEmpty())
            return list.stream().anyMatch(white -> StrUtil.equals(ip, white, true));
        return false;
    }

}
