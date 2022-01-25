package org.hkyaxhfg.tat.feign.conf;

import feign.Client;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Feign的一些默认配置.
 *
 * @author: wjf
 * @date: 2022/1/24
 */
@Configuration
public class FeignConf {

    @Bean
    public Encoder encoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringEncoder(messageConverters);
    }

    @Bean
    public Decoder decoder(ObjectFactory<HttpMessageConverters> messageConverters, ObjectProvider<HttpMessageConverterCustomizer> customizers) {
        return new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters, customizers)));
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
