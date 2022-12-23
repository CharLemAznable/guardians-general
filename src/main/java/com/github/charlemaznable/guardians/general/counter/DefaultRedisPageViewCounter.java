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
public class DefaultRedisPageViewCounter extends EventBusCachedExecutor implements Counter {

    private static final String PV_PREFIX = "PV:";
    private static final DateTimeFormatter PV_DATE_TIME_FORMATTER
            = DateTimeFormat.forPattern("yyyy-MM-dd");

    private final RedissonClient redissonClient;

    @Autowired
    public DefaultRedisPageViewCounter(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 记录PV
     * <p>
     * PV: 某一页面在某一时间段内的访问总量
     */
    @Override
    public void count(HttpServletRequest request) {
        val now = DateTime.now();
        val pvPageAxis = pvPageAxis(request);
        val pvTimeAxis = pvTimeAxis(now);
        post(new PvAxis(pvPageAxis, pvTimeAxis));
    }

    @AllowConcurrentEvents
    @Subscribe
    public void countPV(PvAxis pvAxis) {
        val pvKey = buildPvRedisKey(pvAxis.page + "@" + pvAxis.time);
        redissonClient.getLongAdder(pvKey).increment();
    }

    /**
     * PV计数的页面坐标, 默认为请求路径
     */
    public String pvPageAxis(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * PV计数的时间坐标, 默认为按天计数
     */
    public String pvTimeAxis(DateTime dateTime) {
        return PV_DATE_TIME_FORMATTER.print(dateTime);
    }

    /**
     * PV计数的RedisKey, 默认为: PV:[request-uri]@[yyyy-MM-dd]
     */
    public String buildPvRedisKey(String redisKey) {
        return PV_PREFIX + redisKey;
    }

    @AllArgsConstructor
    private static class PvAxis {

        private String page;
        private String time;
    }
}
