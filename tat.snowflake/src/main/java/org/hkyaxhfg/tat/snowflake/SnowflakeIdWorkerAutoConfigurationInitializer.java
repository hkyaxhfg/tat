package org.hkyaxhfg.tat.snowflake;

import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationLogger;
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
public class SnowflakeIdWorkerAutoConfigurationInitializer {

    @Bean
    @ConditionalOnMissingBean(SnowflakeIdWorker.class)
    public SnowflakeIdWorker snowflakeIdWorker() {
        AutoConfigurationLogger.autoconfigurationInfo("SnowflakeIdWorker");
        return new SnowflakeIdWorker();
    }

}
