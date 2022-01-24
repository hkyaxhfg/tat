package org.hkyaxhfg.tat.feign;

import feign.RequestInterceptor;
import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * OpenFeign的自动配置属性.
 *
 * @author: wjf
 * @date: 2022/1/24
 */
@ConfigurationProperties(AutoConfigurationProperty.OPEN_FEIGN_MAIN_KEY)
public class FeignProperties {
    /**
     * 是否启动feign功能, 默认为true.
     */
    private boolean enabled = true;
    /**
     * 配置的feign-client列表.
     */
    private List<FeignClient<?>> feignClients = Collections.emptyList();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<FeignClient<?>> getFeignClients() {
        return feignClients;
    }

    public void setFeignClients(List<FeignClient<?>> feignClients) {
        this.feignClients = feignClients;
    }

    public static class FeignClient<T> {
        /**
         * feignClient字节码对象.
         */
        private Class<T> feignClientClass;
        /**
         * 网络协议, 默认为HTTP.
         */
        private NetSchema netSchema = NetSchema.HTTP;
        /**
         *  当前feign-client所在的应用名称.
         */
        private String applicationName;
        /**
         * 上下文路径.
         */
        private String contextPath;
        /**
         * feign访问过程中的拦截器.
         */
        private List<RequestInterceptor> interceptors = Collections.emptyList();
        /**
         * 最终生成的feignClient.
         */
        private T feignClientTarget;

        public Class<T> getFeignClientClass() {
            return feignClientClass;
        }

        public void setFeignClientClass(Class<T> feignClientClass) {
            this.feignClientClass = feignClientClass;
        }

        public NetSchema getNetSchema() {
            return netSchema;
        }

        public void setNetSchema(NetSchema netSchema) {
            this.netSchema = netSchema;
        }

        public String getApplicationName() {
            return applicationName;
        }

        public void setApplicationName(String applicationName) {
            this.applicationName = applicationName;
        }

        public String getContextPath() {
            return contextPath;
        }

        public void setContextPath(String contextPath) {
            this.contextPath = contextPath;
        }

        public List<RequestInterceptor> getInterceptors() {
            return interceptors;
        }

        public void setInterceptors(List<RequestInterceptor> interceptors) {
            this.interceptors = interceptors;
        }

        public T getFeignClientTarget() {
            return feignClientTarget;
        }

        public void setFeignClientTarget(T feignClientTarget) {
            this.feignClientTarget = feignClientTarget;
        }

    }

}
