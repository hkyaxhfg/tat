package org.hkyaxhfg.tat.amqp;

import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * AmqpProviderDef: 定义遵循Amqp协议的提供者实现.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
@ConfigurationProperties(prefix = AutoConfigurationProperty.AMQP_PROVIDER_MAIN_KEY)
public class AmqpProviderDef {
    /**
     * 是否启动Amqp自动配置.
     */
    private boolean enabled = AutoConfigurationProperty.AMQP_PROVIDER.isDefaultValue();
    /**
     * amqp描述.
     */
    private String amqpDescription = "";
    /**
     * 交换机定义.
     */
    private List<ExchangeDef> exchangeDefs;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAmqpDescription() {
        return amqpDescription;
    }

    public void setAmqpDescription(String amqpDescription) {
        this.amqpDescription = amqpDescription;
    }

    public List<ExchangeDef> getExchangeDefs() {
        return exchangeDefs;
    }

    public void setExchangeDefs(List<ExchangeDef> exchangeDefs) {
        this.exchangeDefs = exchangeDefs;
    }

}
