package org.hkyaxhfg.tat.auth.oauth2;

/**
 * OAuth2协议的抽象信息承载.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
public abstract class OAuth2Metadata {

    /**
     * OAuth2协议: 应用id.
     */
    protected String appId;
    /**
     * OAuth2协议: 应用密匙.
     */
    protected String appSecret;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
