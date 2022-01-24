package org.hkyaxhfg.tat.autoconfiguration;

/**
 * 自动配置初始化器.
 *
 * @author: wjf
 * @date: 2022/1/10
 */
public interface AutoConfigurationInitializer {

    /**
     * 返回自动配置的日志.
     * @param componentName 组件名称.
     * @return 自动配置的日志.
     */
    static String autoconfigurationInfo(String componentName) {
        return String.format("[%s] 自动配置完成...", componentName);
    }

}
