package org.hkyaxhfg.tat.auth.oauth2;

import org.hkyaxhfg.tat.lang.util.TatException;

/**
 * OAuth2鉴权专用异常.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
public class OAuth2Exception extends TatException {

    public OAuth2Exception(String messagePattern, Object... parameters) {
        super(messagePattern, parameters);
    }

    /**
     * 快捷创建异常的方式.
     * @param messagePattern slf4j模板.
     * @param parameters slf4j模板所需要插入的参数.
     * @return OAuth2Exception
     */
    public static OAuth2Exception newEx(String messagePattern, Object... parameters) {
        return new OAuth2Exception(messagePattern, parameters);
    }

    /**
     * 快捷抛出异常的方式.
     * @param messagePattern slf4j模板.
     * @param parameters slf4j模板所需要插入的参数.
     */
    public static void throwEx(String messagePattern, Object... parameters) {
        throw newEx(messagePattern, parameters);
    }

}
