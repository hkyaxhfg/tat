package org.hkyaxhfg.tat.cache;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 缓存工具类.
 *
 * @author: wjf
 * @date: 2022/1/17
 */
public class CacheUtils {

    private CacheUtils() {}

    public static Map<String, RedisCacheConfiguration> setRedisCacheNameTtl(
            CacheProperties cacheProperties,
            ResourceLoader resourceLoader,
            RedisSerializer<Object> redisSerializer,
            CacheTtl cacheTtl
    ) {
        return RedisUtils.setCacheNameTtl(cacheProperties, resourceLoader, redisSerializer, cacheTtl);
    }

    /**
     * redis工具.
     */
    private static class RedisUtils {

        /**
         * 设置redis缓存ttl.
         * @param cacheProperties 缓存配置.
         * @param resourceLoader 资源加载器.
         * @param redisSerializer redisSerializer.
         * @param cacheTtl cacheTtl.
         * @return Map<String, RedisCacheConfiguration>.
         */
        public static Map<String, RedisCacheConfiguration> setCacheNameTtl(
                CacheProperties cacheProperties,
                ResourceLoader resourceLoader,
                RedisSerializer<Object> redisSerializer,
                CacheTtl cacheTtl
        ) {
            Map<String, RedisCacheConfiguration> redisConfMap = new LinkedHashMap<>();

            Map<String, Long> ttlMap = new LinkedHashMap<>();
            cacheTtl.putCacheTtl(ttlMap);

            ttlMap.forEach((k, v) -> {

                CacheProperties.Redis redisProperties = cacheProperties.getRedis();
                RedisCacheConfiguration redisDefaultConf = RedisCacheConfiguration.defaultCacheConfig();

                redisDefaultConf.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        redisSerializer != null ? redisSerializer : new JdkSerializationRedisSerializer(resourceLoader.getClassLoader())
                ));

                if (!v.equals(CacheTtl.LONG_TERM_EFFECTIVE)) {
                    redisDefaultConf.entryTtl(Duration.ofSeconds(v));
                }
                if (redisProperties.getKeyPrefix() != null) {
                    redisDefaultConf.prefixCacheNameWith(redisProperties.getKeyPrefix());
                }
                if (!redisProperties.isCacheNullValues()) {
                    redisDefaultConf.disableCachingNullValues();
                }
                if (!redisProperties.isUseKeyPrefix()) {
                    redisDefaultConf.disableKeyPrefix();
                }

                redisConfMap.put(k, redisDefaultConf);
            });
            return redisConfMap;
        }

    }

}
