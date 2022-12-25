package io.welldev.initializer.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CacheConfig {
    public final static String BLACKLIST_CACHE_NAME = "jwt-black-list";

//    @Bean
//    RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
//        Long lifeTime = Long.parseLong(System.getenv("TOKEN_EXPIRE_TIME"));
//        return (builder) -> builder
//                    .withCacheConfiguration(BLACKLIST_CACHE_NAME,
//                            RedisCacheConfiguration
//                                    .defaultCacheConfig()
//                                    .entryTtl(Duration.ofHours(lifeTime)));
//
//    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(BLACKLIST_CACHE_NAME);
    }

}
