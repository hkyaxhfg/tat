package org.hkyaxhfg.tat.open;

/**
 * Response: 响应.
 *
 * @author: wjf
 * @date: 2022/2/17
 */
@FunctionalInterface
public interface Response {

    /**
     * 获取此次响应对应的请求.
     * @param <T> Response.
     * @return Request<T>.
     */
    <T extends Response> Request<T> request();

}
