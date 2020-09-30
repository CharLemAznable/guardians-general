package com.github.charlemaznable.guardians.general.visitorcounting;

import com.github.charlemaznable.core.lang.Rand;
import com.github.charlemaznable.core.spring.MutableHttpServletFilter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.charlemaznable.core.codec.Json.unJson;
import static java.lang.Runtime.getRuntime;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CustomUniqueVisitorCounterConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class CustomUniqueVisitorCounterTest {

    public final int COUNT = 1000;
    public final int TIMES = 10000;
    private static MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MutableHttpServletFilter mutableHttpServletFilter;
    @Autowired
    private RedissonClient redissonClient;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    private List<String> usernames = new ArrayList<>();
    private AtomicInteger index = new AtomicInteger(0);

    @BeforeAll
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .addFilters(mutableHttpServletFilter)
                .build();

        for (int i = 0; i < COUNT; i++) {
            usernames.add(Rand.randAlphanumeric(100));
        }
    }

    @SneakyThrows
    @Test
    public void testCustomUniqueVisitor() {
        val now = dateTimeFormatter.print(DateTime.now());

        int threads = getRuntime().availableProcessors() + 1;
        val service = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            service[i] = new Thread(this::batchRun);
            service[i].start();
        }

        for (int i = 0; i < threads; i++) {
            service[i].join();
        }

        await().untilAsserted(() -> {
            val pv = redissonClient.getAtomicLong(
                    "PV:/custom-counter/index@" + now).get();
            assertEquals(TIMES, pv);

            val uv = redissonClient.<String>getHyperLogLog(
                    "UV:/custom-counter/index@" + now).count();
            log.info("result UV: " + uv);
            // redis hyperloglog standard error 0.81%
            assertTrue(uv <= COUNT * 1.0081);
            assertTrue(uv >= COUNT * 0.9919);
        });
    }

    @SneakyThrows
    public void batchRun() {
        while (true) {
            int curr = index.getAndIncrement();
            if (curr >= TIMES) return;

            String username;
            if (curr % 10 == 0) {
                username = usernames.get(curr / 10);
            } else {
                username = usernames.get(Rand.randInt(COUNT));
            }

            val response = mockMvc.perform(get("/custom-counter/index?username=" + username))
                    .andExpect(status().isOk()).andReturn().getResponse();
            val responseContent = response.getContentAsString();
            val responseMap = unJson(responseContent);
            assertEquals("SUCCESS", responseMap.get("result"));
        }
    }
}
