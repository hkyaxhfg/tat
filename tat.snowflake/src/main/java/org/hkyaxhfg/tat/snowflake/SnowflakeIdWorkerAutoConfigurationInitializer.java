package org.hkyaxhfg.tat.snowflake;

import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationInitializer;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花生成器自动配置初始化器.
 *
 * @author: wjf
 * @date: 2022/1/10
 */
@Configuration
@ComponentScan(
        basePackages = {
                "org.hkyaxhfg.tat.snowflake"
        }
)
public class SnowflakeIdWorkerAutoConfigurationInitializer implements AutoConfigurationInitializer {

    private static final Logger logger = LoggerGenerator.logger(SnowflakeIdWorkerAutoConfigurationInitializer.class);

    @Bean
    @ConditionalOnMissingBean(SnowflakeIdWorker.class)
    public SnowflakeIdWorker snowflakeIdWorker() {
        logger.info(AutoConfigurationInitializer.autoconfigurationInfo("SnowflakeIdWorker"));
        return new SnowflakeIdWorker();
    }

}
