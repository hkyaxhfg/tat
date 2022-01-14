package org.hkyaxhfg.tat.snowflake;

import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationInitializer;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花生成器自动配置初始化器.
 *
 * @author: wjf
 * @date: 2022/1/10
 */
@ConfigurationProperties(SnowflakeIdWorkerAutoConfigurationInitializer.SNOWFLAKE_ID_WORKER_AUTO_CONFIGURATION_INITIALIZER)
@Configuration
@ComponentScan(
        basePackages = {
                "org.hkyaxhfg.tat.snowflake"
        }
)
public class SnowflakeIdWorkerAutoConfigurationInitializer implements AutoConfigurationInitializer {

    public static final String SNOWFLAKE_ID_WORKER_AUTO_CONFIGURATION_INITIALIZER = "snowflake-id-worker.autoconfiguration.initializer";

    private static final Logger logger = LoggerGenerator.logger(SNOWFLAKE_ID_WORKER_AUTO_CONFIGURATION_INITIALIZER);

    @Bean
    @ConditionalOnMissingBean(SnowflakeIdWorker.class)
    public SnowflakeIdWorker snowflakeIdWorker() {
        logger.info("SnowflakeIdWorker 初始化默认配置...");
        return new SnowflakeIdWorker();
    }

}
