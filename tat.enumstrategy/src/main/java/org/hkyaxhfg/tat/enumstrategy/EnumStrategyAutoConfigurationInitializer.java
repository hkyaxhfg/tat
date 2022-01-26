package org.hkyaxhfg.tat.enumstrategy;

import com.google.gson.LongSerializationPolicy;
import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationLogger;
import org.hkyaxhfg.tat.lang.json.JSONProcessor;
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
@ConditionalOnProperty(prefix = EnumProperties.PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties(EnumProperties.class)
public class EnumStrategyAutoConfigurationInitializer {

    @Bean
    @ConditionalOnMissingBean(EnumContainer.class)
    public EnumContainer enumContainer(EnumProperties enumProperties) {
        AutoConfigurationLogger.autoconfigurationInfo("EnumContainer");
        return new DefaultEnumContainer(enumProperties);
    }

    @Bean
    @ConditionalOnMissingBean(JSONProcessor.class)
    public JSONProcessor jsonProcessor() {
        AutoConfigurationLogger.autoconfigurationInfo("JSONProcessor");
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
