package org.hkyaxhfg.tat.cache;

import org.apache.commons.lang3.StringUtils;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.hkyaxhfg.tat.lang.util.TatException;
import org.hkyaxhfg.tat.lang.util.Unaware;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 缓存服务.
 *
 * @author: wjf
 * @date: 2022/1/17
 */
public class RedisCacheService {

    private static final Logger logger = LoggerGenerator.logger(RedisCacheService.class);

    private final CacheManager cacheManager;

    private final RedisTemplate<Object, Object> redisTemplate;

    private final CacheProperties.Redis redisProperties;

    public RedisCacheService(CacheManager cacheManager, RedisTemplate<Object, Object> redisTemplate, CacheProperties.Redis redisProperties) {
        this.cacheManager = cacheManager;
        this.redisTemplate = redisTemplate;
        this.redisProperties = redisProperties;
    }

    /**
     * 获取缓存中的值.
     * @param cacheName 缓存名称.
     * @param key key.
     * @param <K> K.
     * @param <V> V.
     * @return V.
     */
    public <K, V> V get(String cacheName, K key) {
        if (key == null) {
            throw TatException.newEx("key不能是空");
        }
        Cache cache = getCache(cacheName);

        Cache.ValueWrapper valueWrapper = cache.get(key);

        return valueWrapper != null ? Unaware.castUnaware(valueWrapper.get()) : null;
    }

    /**
     * 获取缓存中的值.
     * @param cacheName 缓存名称.
     * @param key key.
     * @param valueLoader 值加载器, 当从缓存中获取数据为null时, 就会将值加载器的返回结果放入当前缓存中, 并返回值加载器的结果.
     * @param <K> K.
     * @param <V> V.
     * @return V.
     */
    public <K, V> V get(String cacheName, K key, ValueLoader<K, V> valueLoader) {
        V value = get(cacheName, key);
        if (value != null) {
            return value;
        }

        synchronized (RedisCacheService.class) {
            value = get(cacheName, key);
            if (value != null) {
                return value;
            }

            logger.debug("load value for cache: {}, key: {}", cacheName, key);

            value = valueLoader.load(key);
            //null值不缓存时，直接返回
            if (value == null && !this.redisProperties.isCacheNullValues()) {
                return null;
            }
            Cache cache = getCache(cacheName);
            cache.put(key, value);
            return value;
        }
    }

    /**
     * 存储缓存.
     * @param cacheName 缓存名称.
     * @param key key.
     * @param value value.
     * @param <K> K.
     * @param <V> V.
     */
    public <K, V> void put(String cacheName, K key, V value) {
        Cache cache = getCache(cacheName);

        if (value == null && !redisProperties.isCacheNullValues()) {
            return;
        }

        cache.put(key, value);
    }

    /**
     * 存储缓存.
     * @param cacheName 缓存名称.
     * @param key key.
     * @param value value.
     * @param timeout 超时时间.
     * @param timeUnit 时间单位.
     * @param <K> K.
     * @param <V> V.
     */
    public <K, V> void put(String cacheName, K key, V value, long timeout, TimeUnit timeUnit) {
        if (value == null && !redisProperties.isCacheNullValues()) {
            return;
        }
        String newKey = getKey(cacheName, key);
        redisTemplate.opsForValue().set(newKey, value, timeout, timeUnit);
    }

    /**
     * 清除整片缓存.
     * @param cacheName 缓存名称.
     */
    public void clear(String cacheName) {
        logger.debug("clear cache: {}", cacheName);

        Cache cache = getCache(cacheName);
        cache.clear();
    }

    /**
     * 清除某个缓存.
     * @param cacheName 缓存名称.
     * @param key key.
     * @param <K> K.
     */
    public <K> void evict(String cacheName, K key) {
        if (key == null) {
            throw TatException.newEx("key不能是空");
        }

        logger.debug("evict cache: {}, key: {}", cacheName, key);

        Cache cache = getCache(cacheName);
        cache.evict(key);
    }

    private <K> String getKey(String cacheName, K key) {
        if (!(cacheManager instanceof RedisCacheManager)) {
            throw TatException.newEx("cacheManager 不是 RedisCacheManager的实例");
        }
        RedisCache redisCache = Unaware.castUnaware(getCache(cacheName));

        RedisCacheConfiguration configuration = redisCache.getCacheConfiguration();

        return configuration.usePrefix() ? configuration.getKeyPrefixFor(cacheName) + key : "" + key;
    }

    private Cache getCache(String cacheName) {
        if (StringUtils.isBlank(cacheName)) {
            TatException.throwEx("缓存名称不能为空");
        }
        Cache cache = this.cacheManager.getCache(cacheName);
        if (cache == null) {
            throw TatException.newEx("不存在cacheName为{}的Cache", cacheName);
        }
        return cache;
    }

}
