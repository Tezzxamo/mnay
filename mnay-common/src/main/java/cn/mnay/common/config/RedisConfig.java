package cn.mnay.common.config;


import cn.mnay.common.constant.RedisConstants;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }


    /**
     * 配置缓存管理器
     *
     * @param factory Redis 线程安全连接工厂
     * @return 缓存管理器
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>(8);
        cacheConfigurations.put(RedisConstants.TEST + "1", configuration("cache:test1:", Duration.ofMinutes(10)));
        cacheConfigurations.put(RedisConstants.TEST + "2", configuration("cache:test2:", Duration.ofMinutes(10)));

        // 返回 Redis 缓存管理器
        return RedisCacheManager.builder(factory)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    private RedisCacheConfiguration configuration(String cacheName, Duration ttl) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .prefixCacheNameWith(cacheName)
                .disableCachingNullValues()
                .serializeKeysWith(keyPair())
                .serializeValuesWith(valuePair());
    }

    /**
     * 配置键序列化
     *
     * @return StringRedisSerializer
     */
    private RedisSerializationContext.SerializationPair<String> keyPair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
    }

    /**
     * 配置值序列化，使用 GenericJackson2JsonRedisSerializer 替换默认序列化
     *
     * @return GenericJackson2JsonRedisSerializer
     */
    private RedisSerializationContext.SerializationPair<Object> valuePair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());
    }


}