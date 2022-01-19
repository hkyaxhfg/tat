package org.hkyaxhfg.tat.monitoring;

import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationInitializer;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 监控注解自动配置初始化器.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
@Configuration
@ComponentScan(
        basePackages = {
                "org.hkyaxhfg.tat.monitoring"
        }
)
public class MonitoringAutoConfigurationInitializer implements AutoConfigurationInitializer {

    private static final Logger logger = LoggerGenerator.logger(MonitoringAutoConfigurationInitializer.class);

    @Bean
    @ConditionalOnMissingBean(MonitoringAnnotatedResolver.class)
    public MonitoringAnnotatedResolver monitoringAnnotatedResolver() {
        logger.info(AutoConfigurationInitializer.autoconfigurationInfo("MonitoringAnnotatedResolver"));
        return new MonitoringAnnotatedResolver(new Monitoring.DefaultMonitoringLogic());
    }
}
