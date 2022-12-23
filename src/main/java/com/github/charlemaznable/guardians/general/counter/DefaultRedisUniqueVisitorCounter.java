package com.github.charlemaznable.guardians.general.counter;

import com.github.charlemaznable.core.lang.concurrent.EventBusCachedExecutor;
import com.github.charlemaznable.guardians.general.Counter;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.val;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultRedisUniqueVisitorCounter extends EventBusCachedExecutor implements Counter {

    private static final String UV_PREFIX = "UV:";
    private static final DateTimeFormatter UV_DATE_TIME_FORMATTER
            = DateTimeFormat.forPattern("yyyy-MM-dd");

    private final RedissonClient redissonClient;

    @Autowired
    public DefaultRedisUniqueVisitorCounter(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 记录UV
     * <p>
     * UV: 某一页面在某一时间段内的访问用户总量
     */
    @Override
    public void count(HttpServletRequest request) {
        val now = DateTime.now();
        val uvPageAxis = uvPageAxis(request);
        val uvTimeAxis = uvTimeAxis(now);
        val uvUserIdentify = uvUserIdentify(request);
        post(new UvAxis(uvPageAxis, uvTimeAxis, uvUserIdentify));
    }

    @AllowConcurrentEvents
    @Subscribe
    public void countUV(UvAxis uvAxis) {
        val uvKey = buildUvRedisKey(uvAxis.page + "@" + uvAxis.time);
        redissonClient.<String>getHyperLogLog(uvKey).add(uvAxis.user);
    }

    /**
     * UV计数的页面坐标, 默认为请求路径
     */
    public String uvPageAxis(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * UV计数的时间坐标, 默认为按天计数
     */
    public String uvTimeAxis(DateTime dateTime) {
        return UV_DATE_TIME_FORMATTER.print(dateTime);
    }

    /**
     * UV计数的访问用户唯一判定, 默认为请求来源IP
     */
    public String uvUserIdentify(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    /**
     * UV计数的RedisKey, 默认为: UV:[request-uri]@[yyyy-MM-dd]
     */
    public String buildUvRedisKey(String redisKey) {
        return UV_PREFIX + redisKey;
    }

    @AllArgsConstructor
    private static class UvAxis {

        private String page;
        private String time;
        private String user;
    }
}
