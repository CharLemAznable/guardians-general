package com.github.charlemaznable.guardians.general.uniquenonsense;

import com.github.charlemaznable.guardians.spring.GuardiansImport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import redis.clients.jedis.Jedis;

@EnableWebMvc
@Configuration
@ComponentScan(resourcePattern = "**/SampleRedisUniqueChecker*.class")
@GuardiansImport
public class SampleRedisUniqueCheckerConfiguration {

    @Bean
    public Jedis jedis() {
        return new Jedis("127.0.0.1", 6379);
    }
}
