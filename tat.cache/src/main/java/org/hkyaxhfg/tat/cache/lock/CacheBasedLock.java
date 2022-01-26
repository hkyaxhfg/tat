package org.hkyaxhfg.tat.cache.lock;

/**
 * 基于缓存的锁.
 *
 * @author: wjf
 * @date: 2022/1/26
 */
public interface CacheBasedLock {

    /**
     * 锁, 开启基于缓存的锁.
     * @return 锁是否开启成功.
     * @throws InterruptedException
     */
    public boolean lock() throws InterruptedException;

    /**
     * 释放锁, 关闭基于缓存的锁.
     */
    public void unlock();
}
