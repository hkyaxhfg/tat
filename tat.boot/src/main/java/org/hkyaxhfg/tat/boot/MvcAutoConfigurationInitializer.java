package org.hkyaxhfg.tat.boot;

import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationLogger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * mvc-starter自动配置.
 *
 * @author: wjf
 * @date: 2022/1/12
 */
@Configuration
@ComponentScan(
        basePackages = {
                "org.hkyaxhfg.tat.boot"
        }
)
public class MvcAutoConfigurationInitializer {

    public MvcAutoConfigurationInitializer() {
        AutoConfigurationLogger.autoconfigurationInfo("Mvc");
    }

}
