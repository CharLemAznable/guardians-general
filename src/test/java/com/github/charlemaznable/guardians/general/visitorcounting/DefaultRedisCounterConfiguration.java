package com.github.charlemaznable.guardians.general.visitorcounting;

import com.github.charlemaznable.guardians.spring.GuardiansImport;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.SneakyThrows;
import lombok.val;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import redis.embedded.RedisServer;

@EnableWebMvc
@Configuration
@ComponentScan(resourcePattern = "**/DefaultRedisCounter*.class")
@GuardiansImport
public class DefaultRedisCounterConfiguration {

    private static RedisServer redisServer;

    @SneakyThrows
    @PostConstruct
    public void postConstruct() {
        redisServer = new RedisServer(6383);
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }

    @Bean
    public RedissonClient redissonClient() {
        val config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6383");
        return Redisson.create(config);
    }
}
