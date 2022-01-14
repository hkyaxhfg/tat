package org.hkyaxhfg.tat.auth.user;

import org.hkyaxhfg.tat.lang.util.TatException;

/**
 * 用户鉴权专用异常.
 *
 * @author: wjf
 * @date: 2022/1/11
 */
public class UserAuthException extends TatException {

    public UserAuthException(String messagePattern, Object... parameters) {
        super(messagePattern, parameters);
    }

    /**
     * 快捷创建异常的方式.
     * @param messagePattern slf4j模板.
     * @param parameters slf4j模板所需要插入的参数.
     * @return UserAuthException
     */
    public static UserAuthException newEx(String messagePattern, Object... parameters) {
        return new UserAuthException(messagePattern, parameters);
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
