package org.hkyaxhfg.tat.mvcstarter;

/**
 * 业务编码code.
 *
 * @author: wjf
 * @date: 2022/1/21
 */
public interface BizStateCode {

    /**
     * 返回此业务对应的异常状态码.
     * @return stateCode.
     */
    int stateCode();

    /**
     * 此异常状态码对应的描述信息.
     * @return message.
     */
    String message();

}
