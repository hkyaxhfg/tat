package org.hkyaxhfg.tat.mvcstarter;

import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationInitializer;
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
                "org.hkyaxhfg.tat.mvcstarter"
        }
)
public class MvcAutoConfigurationInitializer implements AutoConfigurationInitializer {}
