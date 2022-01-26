package org.hkyaxhfg.tat.cache;

import java.util.Map;

/**
 * 缓存过期时间设置.
 *
 * @author: wjf
 * @date: 2022/1/17
 */
@FunctionalInterface
public interface CacheTtl {

    /**
     * 长期有效, 永不过期.
     */
    public static final long LONG_TERM_EFFECTIVE = -1;

    /**
     * 设置缓存的过期时间, key为缓存名称, value为对应的过期时间, 单位秒(s).
     * @param ttlMap ttlMap.
     */
     void putCacheTtl(Map<String, Long> ttlMap);

}
