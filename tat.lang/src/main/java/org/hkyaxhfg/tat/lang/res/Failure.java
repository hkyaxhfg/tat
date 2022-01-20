package org.hkyaxhfg.tat.lang.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hkyaxhfg.tat.lang.util.TatException;

import java.util.Optional;

/**
 * @author: wjf
 * @date: 2022/1/20
 */
public class Failure<T extends Throwable> extends Result<T, Failure<T>>{
    /**
     * 当前异常.
     */
    @JsonIgnore
    private transient T throwable;
    /**
     * 面向开发人员的错误信息.
     */
    private String throwableMessage;
    /**
     * 开发人员自定义的错误信息.
     */
    private String customizeMessage;
    /**
     * 面向用户的错误信息.
     */
    private String originate;

    public Failure(T throwable) {
        this(HttpServerState.Internal_Server_Error, throwable);
    }

    public Failure(HttpServerState serverState, T throwable) {
        this(serverState.getState(), serverState.getCnMessage(), throwable);
    }

    public Failure(int state, String message, T throwable) {
        this(state, message, throwable, null, null);
    }

    public Failure(int state, String message, T throwable, String customizeMessage, String originate) {
        super(state, message);
        this.throwable = throwable;
        if (this.throwable != null) {
            this.throwableMessage = this.throwable.toString();
        }
        this.customizeMessage = customizeMessage;
        this.originate = originate;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public Failure<T> isSuccessAndGet() {
        throw TatException.newEx("Result is Failure");
    }

    @Override
    public boolean isFailure() {
        return true;
    }

    @Override
    public Failure<T> isFailureAndGet() {
        return this;
    }

    @Override
    public Optional<T> get() {
        return Optional.of(throwable);
    }

    public T getThrowable() {
        return throwable;
    }

    public void setThrowable(T throwable) {
        this.throwable = throwable;
    }

    public String getThrowableMessage() {
        return throwableMessage;
    }

    public void setThrowableMessage(String throwableMessage) {
        this.throwableMessage = throwableMessage;
    }

    public String getCustomizeMessage() {
        return customizeMessage;
    }

    public void setCustomizeMessage(String customizeMessage) {
        this.customizeMessage = customizeMessage;
    }

    public String getOriginate() {
        return originate;
    }

    public void setOriginate(String originate) {
        this.originate = originate;
    }
}
