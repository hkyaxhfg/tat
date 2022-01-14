package org.hkyaxhfg.tat.springfox;

import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationInitializer;
import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationProperty;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * springfox自动配置器.
 *
 * @author: wjf
 * @date: 2022/1/12
 */
@ConfigurationProperties(SpringfoxAutoConfigurationInitializer.SPRINGFOX_AUTO_CONFIGURATION_INITIALIZER)
@Configuration
@ComponentScan(
        basePackages = {
                "org.hkyaxhfg.tat.springfox"
        }
)
@ConditionalOnProperty(prefix = AutoConfigurationProperty.SPRINGFOX_MAIN_KEY, name = "enabled", havingValue = "true")
@EnableOpenApi
@EnableConfigurationProperties(SpringfoxProperties.class)
public class SpringfoxAutoConfigurationInitializer implements AutoConfigurationInitializer {

    public static final String SPRINGFOX_AUTO_CONFIGURATION_INITIALIZER = "springfox.autoconfiguration.initializer";

    private static final Logger logger = LoggerGenerator.logger(SPRINGFOX_AUTO_CONFIGURATION_INITIALIZER);

    @Bean
    @ConditionalOnMissingBean(SpringfoxDefiner.class)
    public SpringfoxDefiner springfoxDefiner(SpringfoxProperties springfoxProperties, Environment environment) {
        logger.info("SpringfoxDefiner 初始化默认配置...");
        return new DefaultSpringfoxDefiner(springfoxProperties, environment);
    }

    @Bean
    @ConditionalOnBean(SpringfoxDefiner.class)
    public Docket docket(SpringfoxDefiner springfoxDefiner) {
        logger.info("Springfox 初始化默认配置...");
        return springfoxDefiner.definition();
    }
}
