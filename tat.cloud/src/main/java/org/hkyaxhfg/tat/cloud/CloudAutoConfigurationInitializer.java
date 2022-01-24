package org.hkyaxhfg.tat.cloud;

import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationInitializer;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.slf4j.Logger;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 云自动配置.
 *
 * @author: wjf
 * @date: 2022/1/24
 */
@Configuration
@ComponentScan(
        basePackages = {
                "org.hkyaxhfg.tat.cloud"
        }
)
@EnableDiscoveryClient
public class CloudAutoConfigurationInitializer implements AutoConfigurationInitializer {

    private static final Logger logger = LoggerGenerator.logger(CloudAutoConfigurationInitializer.class);

    {
        logger.info(AutoConfigurationInitializer.autoconfigurationInfo("Spring-Cloud"));
    }

}
