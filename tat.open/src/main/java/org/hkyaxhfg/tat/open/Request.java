package org.hkyaxhfg.tat.open;

/**
 * Request: 请求, 一个Request对应一个特定的Response.
 *
 * @author: wjf
 * @date: 2022/2/17
 */
@FunctionalInterface
public interface Request<T extends Response> {

    /**
     * 获取请求id.
     * @return String.
     */
    String requestId();

}
