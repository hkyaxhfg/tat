package org.hkyaxhfg.tat.autoconfiguration;

/**
 * 自动配置分组.
 *
 * @author: wjf
 * @date: 2022/1/14
 */
public enum AutoConfigurationProperty {

    ENUM("enabled", false),
    SPRINGFOX("enabled", true),
    AMQP_PROVIDER("enabled", false),
    AMQP_CONSUMER("enabled", false),
    OPEN_FEIGN("enabled", true),
    JOB("enabled", true),

    ;

    public static final String ENUM_MAIN_KEY = "hkyaxhfg.tat.enum";

    public static final String SPRINGFOX_MAIN_KEY = "hkyaxhfg.tat.springfox";

    public static final String AMQP_PROVIDER_MAIN_KEY = "hkyaxhfg.tat.amqp-provider-def";

    public static final String AMQP_CONSUMER_MAIN_KEY = "hkyaxhfg.tat.amqp-consumer-def";

    public static final String OPEN_FEIGN_MAIN_KEY = "hkyaxhfg.tat.feign";

    public static final String JOB_MAIN_KEY = "hkyaxhfg.tat.job";

    private final String enabledKey;

    private final boolean defaultValue;

    AutoConfigurationProperty(String enabledKey, boolean defaultValue) {
        this.enabledKey = enabledKey;
        this.defaultValue = defaultValue;
    }

    public String getEnabledKey() {
        return enabledKey;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

}
