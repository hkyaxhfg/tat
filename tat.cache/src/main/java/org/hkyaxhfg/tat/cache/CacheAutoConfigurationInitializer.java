package org.hkyaxhfg.tat.cache;

import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationInitializer;
import org.hkyaxhfg.tat.lang.util.TatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

/**
 * 缓存自动配置器.
 *
 * @author: wjf
 * @date: 2022/1/17
 */
@ConfigurationProperties(CacheAutoConfigurationInitializer.CACHE_AUTO_CONFIGURATION_INITIALIZER)
@Configuration
@ComponentScan(
        basePackages = {
                "org.hkyaxhfg.tat.cache"
        }
)
@EnableConfigurationProperties(CacheProperties.class)
public class CacheAutoConfigurationInitializer implements AutoConfigurationInitializer {

    public static final String CACHE_AUTO_CONFIGURATION_INITIALIZER = "cache.autoconfiguration.initializer";

    @Configuration
    public static class BaseCacheConf extends CachingConfigurerSupport {

        private final CacheProperties cacheProperties;

        @Autowired
        public BaseCacheConf(CacheProperties cacheProperties) {
            this.cacheProperties = cacheProperties;
        }

        @Bean
        public RedisSerializer<Object> redisSerializer() {
            return new JdkSerializationRedisSerializer();
        }

        @Bean
        @ConditionalOnMissingBean(CacheTtl.class)
        public CacheTtl cacheTtl() {
            return new CacheTtl() {
                @Override
                public void putCacheTtl(Map<String, Long> ttlMap) {

                }
            };
        }

        @Bean
        public RedisCacheManager redisCacheManager(
                RedisConnectionFactory redisConnectionFactory,
                ResourceLoader resourceLoader,
                RedisSerializer<Object> redisSerializer,
                CacheTtl cacheTtl
        ) {
            if (cacheTtl == null) {
                TatException.throwEx("请配置 [org.hkyaxhfg.tat.cache.CacheTtl] 实例, 并交给spring管理");
            }

            return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
                    .withInitialCacheConfigurations(
                            CacheUtils.setRedisCacheNameTtl(
                                    cacheProperties,
                                    resourceLoader,
                                    redisSerializer,
                                    cacheTtl
                            )
                    ).build();
        }

        @Bean
        @ConditionalOnMissingBean(name = "redisTemplate")
        @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
        public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, RedisSerializer<Object> redisSerializer) {
            StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

            RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory);
            redisTemplate.setKeySerializer(stringRedisSerializer);
            redisTemplate.setValueSerializer(redisSerializer);
            redisTemplate.setHashKeySerializer(stringRedisSerializer);
            redisTemplate.setValueSerializer(stringRedisSerializer);
            return redisTemplate;
        }

        @Bean
        public RedisCacheService redisCacheService(RedisCacheManager redisCacheManager, RedisTemplate<Object, Object> redisTemplate) {
            return new RedisCacheService(redisCacheManager, redisTemplate, cacheProperties.getRedis());
        }

    }

}
