package org.hkyaxhfg.tat.enumstrategy;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 枚举的自动配置属性.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
@ConfigurationProperties(EnumProperties.PREFIX)
public class EnumProperties {

    public static final String PREFIX = "hkyaxhfg.tat.enum";

    /**
     * 是否启动枚举相关组件, 默认为false.
     */
    private boolean enabled = false;
    /**
     * 枚举所在的包名, 多个包名用 [,] 隔开.
     */
    private String scans;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getScans() {
        return scans;
    }

    public void setScans(String scans) {
        this.scans = scans;
    }
}
