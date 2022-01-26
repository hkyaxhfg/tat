package org.hkyaxhfg.tat.springfox;

import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@Configuration
@ComponentScan(
        basePackages = {
                "org.hkyaxhfg.tat.springfox"
        }
)
@ConditionalOnProperty(prefix = SpringfoxProperties.PREFIX, name = "enabled", havingValue = "true")
@EnableOpenApi
@EnableConfigurationProperties(SpringfoxProperties.class)
public class SpringfoxAutoConfigurationInitializer {

    @Bean
    @ConditionalOnMissingBean(SpringfoxDefiner.class)
    public SpringfoxDefiner springfoxDefiner(SpringfoxProperties springfoxProperties, Environment environment) {
        AutoConfigurationLogger.autoconfigurationInfo("SpringfoxDefiner");
        return new DefaultSpringfoxDefiner(springfoxProperties, environment);
    }

    @Bean
    @ConditionalOnBean(SpringfoxDefiner.class)
    public Docket docket(SpringfoxDefiner springfoxDefiner) {
        AutoConfigurationLogger.autoconfigurationInfo("Springfox");
        return springfoxDefiner.definition();
    }
}
