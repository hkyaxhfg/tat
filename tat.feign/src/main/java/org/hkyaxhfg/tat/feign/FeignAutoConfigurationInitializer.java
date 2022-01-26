package org.hkyaxhfg.tat.feign;

import feign.Client;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationLogger;
import org.hkyaxhfg.tat.feign.conf.FeignConf;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * open-feign自动配置.
 *
 * @author: wjf
 * @date: 2022/1/24
 */
@Configuration
@ComponentScan(
        basePackages = {
                "org.hkyaxhfg.tat.feign"
        }
)
@ConditionalOnProperty(prefix = FeignProperties.PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties(FeignProperties.class)
@Import({FeignConf.class, FeignAutoConfiguration.class})
@EnableFeignClients
public class FeignAutoConfigurationInitializer {

    @Bean
    @ConditionalOnMissingBean(FeignClientContainer.class)
    public FeignClientContainer feignClientContainer(Client client, Encoder encoder, Decoder decoder, FeignProperties feignProperties) {
        AutoConfigurationLogger.autoconfigurationInfo("Open-Feign");
        return new DefaultFeignClientContainer(client, encoder, decoder, feignProperties);
    }

}
