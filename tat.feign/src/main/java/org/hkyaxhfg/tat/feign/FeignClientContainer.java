package org.hkyaxhfg.tat.feign;

/**
 * feign-client容器, 用来装feign-client.
 *
 * @author: wjf
 * @date: 2022/1/24
 */
public interface FeignClientContainer {
    /**
     * 放入feign-client到容器中.
     * @param feignClient feign-client.
     */
    <T> void put(FeignProperties.FeignClient<T> feignClient);

    /**
     * 从容器获取feign-client.
     * @param feignClientClass feignClientClass.
     * @param <T> T.
     * @return T.
     */
    <T> T get(Class<T> feignClientClass);

}
