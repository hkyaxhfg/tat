package org.hkyaxhfg.tat.auth;

/**
 * 鉴权接口, 一些鉴权相关的基本操作.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
@FunctionalInterface
public interface Auth<Info> {

    /**
     * 获取鉴权信息.
     * @return 鉴权信息.
     */
    Info get();

}
