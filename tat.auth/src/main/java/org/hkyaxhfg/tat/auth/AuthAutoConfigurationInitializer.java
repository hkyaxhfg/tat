package org.hkyaxhfg.tat.auth;

import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationLogger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wjf
 * @date: 2022/1/24
 */
@Configuration
@ComponentScan(
        basePackages = {
                "org.hkyaxhfg.tat.auth"
        }
)
public class AuthAutoConfigurationInitializer {

    public AuthAutoConfigurationInitializer() {
        AutoConfigurationLogger.autoconfigurationInfo("Auth");
    }

}
