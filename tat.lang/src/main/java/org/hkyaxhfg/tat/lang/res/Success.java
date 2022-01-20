package org.hkyaxhfg.tat.lang.res;

import org.hkyaxhfg.tat.lang.util.TatException;
import org.hkyaxhfg.tat.lang.util.Unaware;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 成功时的结果.
 *
 * @author: wjf
 * @date: 2022/1/20
 */
public class Success<T> extends Result<T, Success<T>> {
    /**
     * 数据.
     */
    public T data;
    /**
     * 额外附带的数据.
     */
    public Map<String, Object> options;

    public Success(T data) {
        this(HttpServerState.Ok, data);
    }

    public Success(HttpServerState serverState, T data) {
        this(serverState.getState(), serverState.getCnMessage(), data);
    }

    public Success(int state, String message, T data) {
        super(state, message);
        this.data = data;
    }

    public Success<T> addOption(String key, Object option) {
        if (options == null) {
            options = new LinkedHashMap<>();
        }
        this.options.put(key, option);
        return this;
    }

    public <V> V getOption(String key) {
        return Unaware.castUnaware(this.options.get(key));
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public Success<T> isSuccessAndGet() {
        return this;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public Success<T> isFailureAndGet() {
        throw TatException.newEx("Result is Success");
    }

    @Override
    public Optional<T> get() {
        return Optional.of(data);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }
}
