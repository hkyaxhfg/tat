package org.hkyaxhfg.tat.lang.res;

import java.io.Serializable;
import java.util.Optional;

/**
 * 结果.
 *
 * @author: wjf
 * @date: 2022/1/20
 */
public abstract class Result<T, R extends Result<?, ?>> implements Serializable {

    /**
     * 状态位.
     */
    protected int state;
    /**
     * 消息描述.
     */
    protected String message;

    protected Result(int state, String message) {
        this.state = state;
        this.message = message;
    }

    /**
     * 是否成功.
     * @return boolean.
     */
    public abstract boolean isSuccess();

    /**
     * 是成功同时获取, 否则抛出异常.
     * @return Optional<R>.
     */
    public abstract R isSuccessAndGet();

    /**
     * 是否失败.
     * @return boolean.
     */
    public abstract boolean isFailure();

    /**
     * 是失败同时获取, 否则抛出异常.
     * @return Optional<R>.
     */
    public abstract R isFailureAndGet();

    /**
     * 获取内部数据.
     * @return Optional<T>.
     */
    public abstract Optional<T> get();

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
