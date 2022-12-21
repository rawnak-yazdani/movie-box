package io.welldev.initializer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ComponentScan({"io.welldev.initializer.configuration.userauth"})
public class CacheConfig {
    public final static String BLACKLIST_CACHE_NAME = "jwt-black-list";

//    @Value("${spring.redis.host:localhost}")
//    private String redisHost;
//
//    @Value("${spring.redis.port:7001}")
//    private int redisPort;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        Long lifeTime = Long.parseLong(System.getenv("TOKEN_EXPIRE_TIME"));
        return (builder) -> {
            Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
            configurationMap.put(BLACKLIST_CACHE_NAME, RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofHours(lifeTime)));
            builder.withInitialCacheConfigurations(configurationMap);
        };
    }
}
