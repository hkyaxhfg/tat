package org.hkyaxhfg.tat.enumstrategy;

import com.google.gson.LongSerializationPolicy;
import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationInitializer;
import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationProperty;
import org.hkyaxhfg.tat.lang.json.JSONProcessor;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 枚举策略自动配置器.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
@Configuration
@ComponentScan(
        basePackages = {
                "org.hkyaxhfg.tat.enumstrategy"
        }
)
@ConditionalOnProperty(prefix = AutoConfigurationProperty.ENUM_MAIN_KEY, name = "enabled", havingValue = "true")
@EnableConfigurationProperties(EnumProperties.class)
public class EnumStrategyAutoConfigurationInitializer implements AutoConfigurationInitializer {

    private static final Logger logger = LoggerGenerator.logger(EnumStrategyAutoConfigurationInitializer.class);

    @Bean
    @ConditionalOnMissingBean(EnumContainer.class)
    public EnumContainer enumContainer(EnumProperties enumProperties) {
        logger.info(AutoConfigurationInitializer.autoconfigurationInfo("EnumContainer"));
        return new DefaultEnumContainer(enumProperties);
    }

    @Bean
    @ConditionalOnMissingBean(JSONProcessor.class)
    public JSONProcessor jsonProcessor() {
        logger.info(AutoConfigurationInitializer.autoconfigurationInfo("JSONProcessor"));
        return new JSONProcessor(
                gsonBuilder -> {
                    gsonBuilder
                            .serializeNulls()
                            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                            .registerTypeAdapter(EnumStrategy.class, new EnumTypeAdapter());
                }
        );
    }

}
