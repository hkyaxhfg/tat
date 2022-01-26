package org.hkyaxhfg.tat.job;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationInitializer;
import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationProperty;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.slf4j.Logger;
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
@ConditionalOnProperty(prefix = AutoConfigurationProperty.JOB_MAIN_KEY, name = "enabled", havingValue = "true")
public class JobAutoConfigurationInitializer implements AutoConfigurationInitializer {

    private static final Logger logger = LoggerGenerator.logger(JobAutoConfigurationInitializer.class);

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

        logger.info(AutoConfigurationInitializer.autoconfigurationInfo("XXL-Job"));
        return xxlJobSpringExecutor;
    }

}
