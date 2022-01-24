package org.hkyaxhfg.tat.feign;

import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.hkyaxhfg.tat.lang.util.Container;
import org.hkyaxhfg.tat.lang.util.Unaware;
import org.springframework.cloud.openfeign.support.SpringMvcContract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的feign-client容器.
 *
 * @author: wjf
 * @date: 2022/1/24
 */
public class DefaultFeignClientContainer implements FeignClientContainer {

    private final ConcurrentHashMap<String, List<FeignProperties.FeignClient<?>>> feignClientMap = new ConcurrentHashMap<>();

    private final Client client;

    private final Encoder encoder;

    private final Decoder decoder;

    public DefaultFeignClientContainer(Client client, Encoder encoder, Decoder decoder) {
        this.client = client;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    @Override
    public <T> void put(FeignProperties.FeignClient<T> feignClient) {
        String key = feignClient.getApplicationName() + feignClient.getContextPath();
        initFeignClient(feignClient);
        if (feignClientMap.containsKey(key)) {
            List<FeignProperties.FeignClient<?>> feignClients = feignClientMap.get(key);
            feignClients.add(feignClient);
        } else {
            List<FeignProperties.FeignClient<?>> feignClients = new ArrayList<>();
            feignClients.add(feignClient);
            feignClientMap.put(key, feignClients);
        }
    }

    @Override
    public <T> T get(Class<T> feignClientClass) {
        Container<T> container = new Container<>();
        feignClientMap.forEach((k, v) -> {
            v.forEach(feignClient -> {
                if (feignClient.getFeignClientClass() == feignClientClass) {
                    container.put(Unaware.castUnaware(feignClient.getFeignClientTarget()));
                }
            });
        });
        return container.get();
    }

    private <T> void initFeignClient(FeignProperties.FeignClient<T> feignClient) {
        feignClient.setFeignClientTarget(
                Feign.builder()
                        .client(client)
                        .encoder(encoder)
                        .decoder(decoder)
                        .contract(new SpringMvcContract())
                        .requestInterceptors(feignClient.getInterceptors())
                        .target(feignClient.getFeignClientClass(), feignClientUrl(feignClient))
        );
    }

    private String feignClientUrl(FeignProperties.FeignClient<?> feignClient) {
        StringBuilder builder = new StringBuilder(feignClient.getNetSchema().name().toLowerCase());
        builder.append(":").append("//");
        builder.append(feignClient.getApplicationName()).append(feignClient.getContextPath());
        return builder.toString();
    }

}
