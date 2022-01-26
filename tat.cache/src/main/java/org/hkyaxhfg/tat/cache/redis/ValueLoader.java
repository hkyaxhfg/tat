package org.hkyaxhfg.tat.cache.redis;

/**
 * 值加载器.
 *
 * @author: wjf
 * @date: 2022/1/17
 */
public interface ValueLoader<K, V> {

    /**
     * 加载值.
     * @param key key.
     * @return V.
     */
    V load(K key);

}
