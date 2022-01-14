package org.hkyaxhfg.tat.autoconfiguration;

/**
 * 自动配置分组.
 *
 * @author: wjf
 * @date: 2022/1/14
 */
public enum AutoConfigurationProperty {

    ENUM("enabled", false),
    SPRINGFOX("enabled", true)
    ;

    public static final String ENUM_MAIN_KEY = "hkyaxhfg.tat.enum";

    public static final String SPRINGFOX_MAIN_KEY = "hkyaxhfg.tat.springfox";

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
