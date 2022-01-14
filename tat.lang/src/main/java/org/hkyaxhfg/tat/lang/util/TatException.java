package org.hkyaxhfg.tat.lang.util;

import org.slf4j.helpers.MessageFormatter;

/**
 * 自定义异常, 只在框架内部使用.
 *
 * @author: wjf
 * @date: 2022/1/6
 */
public class TatException extends RuntimeException {

    /**
     * 构造方法, 支持slf4j模板插值.
     * @param messagePattern slf4j模板.
     * @param parameters slf4j模板所需要插入的参数.
     */
    public TatException(String messagePattern, Object... parameters) {
        super(MessageFormatter.arrayFormat(messagePattern, parameters).getMessage());
    }

    /**
     * 快捷创建异常的方式.
     * @param messagePattern slf4j模板.
     * @param parameters slf4j模板所需要插入的参数.
     * @return TatException
     */
    public static TatException newEx(String messagePattern, Object... parameters) {
        return new TatException(messagePattern, parameters);
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
