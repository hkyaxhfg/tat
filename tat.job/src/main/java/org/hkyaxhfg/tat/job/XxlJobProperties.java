package org.hkyaxhfg.tat.job;

import org.hkyaxhfg.tat.autoconfiguration.AutoConfigurationProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * xxl-job定时任务.
 *
 * @author: wjf
 * @date: 2022/1/26
 */
@ConfigurationProperties(AutoConfigurationProperty.JOB_MAIN_KEY)
public class XxlJobProperties {
    /**
     * 是否启动xxl-job自动配置, 默认为true.
     */
    private boolean enabled = AutoConfigurationProperty.JOB.isDefaultValue();
    /**
     * xxl-job的admin后台地址.
     */
    private String adminAddresses;
    /**
     * xxl-job执行器的应用名称.
     */
    private String appName;
    /**
     * xxl-job执行器的ip.
     */
    private String ip;
    /**
     * xxl-job执行器的端口.
     */
    private int port;
    /**
     * xxl-job的访问凭据.
     */
    private String accessToken;
    /**
     * xxl-job的执行日志文件路径.
     */
    private String logPath;
    /**
     * xxl-job的执行日志文件保留天数.
     */
    private int logRetentionDays;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAdminAddresses() {
        return adminAddresses;
    }

    public void setAdminAddresses(String adminAddresses) {
        this.adminAddresses = adminAddresses;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public int getLogRetentionDays() {
        return logRetentionDays;
    }

    public void setLogRetentionDays(int logRetentionDays) {
        this.logRetentionDays = logRetentionDays;
    }
}
