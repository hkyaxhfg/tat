package org.hkyaxhfg.tat.job;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 定时任务自动配置.
 *
 * @author: wjf
 * @date: 2022/1/26
 */
@Configuration
@ComponentScan(
        basePackages = {
                "org.hkyaxhfg.tat.job"
        }
)
@EnableConfigurationProperties(XxlJobProperties.class)
@ConditionalOnProperty(prefix = XxlJobProperties.PREFIX, name = "enabled", havingValue = "true")
public class JobAutoConfigurationInitializer {

    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor(XxlJobProperties xxlJobProperties) {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getAppName());
        xxlJobSpringExecutor.setIp(xxlJobProperties.getIp());
        xxlJobSpringExecutor.setPort(xxlJobProperties.getPort());
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());

        AutoConfigurationLogger.autoconfigurationInfo("Xxl-Job");
        return xxlJobSpringExecutor;
    }

}
