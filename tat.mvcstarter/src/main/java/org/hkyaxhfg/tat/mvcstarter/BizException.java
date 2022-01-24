package org.hkyaxhfg.tat.mvcstarter;

import org.hkyaxhfg.tat.lang.util.TatException;

/**
 * 业务异常.
 *
 * @author: wjf
 * @date: 2022/1/21
 */
public class BizException extends TatException {

    private final int state;

    public BizException(String messagePattern, Object... parameters) {
        this(-1, messagePattern, parameters);
    }

    public BizException(BizStateCode bizStateCode) {
        this(bizStateCode.stateCode(), bizStateCode.message());
    }

    public BizException(int state, String messagePattern, Object... parameters) {
        super(messagePattern, parameters);
        this.state = state;
    }

    /**
     * 快捷创建异常的方式.
     * @param messagePattern slf4j模板.
     * @param parameters slf4j模板所需要插入的参数.
     * @return UserAuthException.
     */
    public static BizException newEx(String messagePattern, Object... parameters) {
        return new BizException(messagePattern, parameters);
    }

    /**
     * 快捷抛出异常的方式.
     * @param messagePattern slf4j模板.
     * @param parameters slf4j模板所需要插入的参数.
     */
    public static void throwEx(String messagePattern, Object... parameters) {
        throw newEx(messagePattern, parameters);
    }

    public int getState() {
        return state;
    }
}
