package org.hkyaxhfg.tat.cache.lock;

import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Objects;

/**
 * redis锁.
 *
 * @author: wjf
 * @date: 2022/1/26
 */
public class RedisLock implements CacheBasedLock {

    private static final Logger logger = LoggerGenerator.logger(RedisLock.class);

    private final RedisTemplate<Object, Object> redisTemplate;
    private static final int DEFAULT_PAUSE_STEP_MILLIS = 100;
    private final String lockKey;
    private int expireMsecs;
    private int timeoutMsecs;
    private volatile boolean locked;

    public RedisLock(RedisTemplate<Object, Object> redisTemplate, String lockKey) {
        this.expireMsecs = 60000;
        this.timeoutMsecs = 10000;
        this.locked = false;
        this.redisTemplate = redisTemplate;
        this.lockKey = lockKey + "_redis-lock";
    }

    public RedisLock(RedisTemplate<Object, Object> redisTemplate, String lockKey, int timeoutMsecs) {
        this(redisTemplate, lockKey);
        this.timeoutMsecs = timeoutMsecs;
    }

    public RedisLock(RedisTemplate<Object, Object> redisTemplate, String lockKey, int timeoutMsecs, int expireMsecs) {
        this(redisTemplate, lockKey, timeoutMsecs);
        this.expireMsecs = expireMsecs;
    }

    @Override
    @SuppressWarnings("all")
    public synchronized boolean lock() throws InterruptedException {
        /*
         *  cas: 比较并交换思想.
         */
        int timeout = this.timeoutMsecs;

        while(timeout >= 0) {
            long expires = System.currentTimeMillis() + this.expireMsecs + 1L;
            String expiresStr = String.valueOf(expires);
            if (this.setNX(this.lockKey, expiresStr)) {
                this.locked = true;
                return true;
            }

            String currentValueStr = this.get(this.lockKey);
            if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                String oldValueStr = this.set(this.lockKey, expiresStr);
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    this.locked = true;
                    return true;
                }
            }

            timeout -= DEFAULT_PAUSE_STEP_MILLIS;
            Thread.sleep(DEFAULT_PAUSE_STEP_MILLIS);
        }

        return false;
    }

    @Override
    public synchronized void unlock() {
        if (this.locked) {
            this.redisTemplate.delete(this.lockKey);
            this.locked = false;
        }
    }

    public String getLockKey() {
        return this.lockKey;
    }

    /**
     * 获取key对应的value.
     * @param key key.
     * @return value.
     */
    private String get(final String key) {
        Object obj = null;

        try {
            obj = this.redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                @SuppressWarnings("all")
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    byte[] data = connection.get(Objects.requireNonNull(serializer.serialize(key)));
                    connection.close();
                    return data == null ? null : serializer.deserialize(data);
                }
            });
        } catch (Exception var4) {
            logger.error("get redis error, key : {}", key);
        }

        return obj != null ? obj.toString() : null;
    }

    /**
     * 设置key的value, 仅当key不存在时.
     * @param key key.
     * @return value.
     * @return 是否设置成功.
     */
    private boolean setNX(final String key, final String value) {
        Object obj = null;

        try {
            obj = this.redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                @SuppressWarnings("all")
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    Boolean success = connection.setNX(Objects.requireNonNull(serializer.serialize(key)), Objects.requireNonNull(serializer.serialize(value)));
                    connection.close();
                    return success;
                }
            });
        } catch (Exception var5) {
            logger.error("setNX redis error, key : {}", key);
        }

        return obj != null && (Boolean)obj;
    }

    /**
     * 设置key所对应的value, 并返回旧值.
     * @param key key.
     * @param value value.
     * @return 旧值.
     */
    private String set(final String key, final String value) {
        Object obj = null;

        try {
            obj = this.redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                @SuppressWarnings("all")
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    byte[] ret = connection.getSet(Objects.requireNonNull(serializer.serialize(key)), Objects.requireNonNull(serializer.serialize(value)));
                    connection.close();
                    return serializer.deserialize(ret);
                }
            });
        } catch (Exception var5) {
            logger.error("setNX redis error, key : {}", key);
        }

        return obj != null ? (String)obj : null;
    }

}
