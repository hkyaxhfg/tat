package org.hkyaxhfg.tat.autoconfiguration;

import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.slf4j.Logger;

/**
 * 自动配置日志记录者.
 *
 * @author: wjf
 * @date: 2022/1/26
 */
public final class AutoConfigurationLogger {

    private static final Logger logger = LoggerGenerator.logger(AutoConfigurationLogger.class);

    private AutoConfigurationLogger() {}

    /**
     * 返回自动配置的日志.
     * @param componentName 组件名称.
     */
    public static void autoconfigurationInfo(String componentName) {
        logger.info("[{}] 自动配置完成...", componentName);
    }

}
