package org.hkyaxhfg.tat.lang.res;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import org.hkyaxhfg.tat.lang.util.Unaware;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Page工具.
 *
 * @author: wjf
 * @date: 2022/1/20
 */
public class ResultUtils {
    /**
     * 默认当前页.
     */
    private final int defaultPageNum;
    /**
     * 默认分页条数.
     */
    private final int defaultPageSize;

    /**
     * 构造方法.
     * @param defaultPageNum 默认当前页.
     * @param defaultPageSize 默认分页条数.
     */
    public ResultUtils(int defaultPageNum, int defaultPageSize) {
        this.defaultPageNum = defaultPageNum;
        this.defaultPageSize = defaultPageSize;
    }

    /**
     * 设置分页.
     */
    public void startPage() {
        PageMethod.startPage(defaultPageNum, defaultPageSize);
    }

    /**
     * 设置分页.
     * @param pageNum 当前页.
     * @param pageSize 分页条数.
     */
    public static void startPage(int pageNum, int pageSize) {
        PageMethod.startPage(pageNum, pageSize);
    }

    /**
     * list转换.
     * @param list 源.
     * @param function 转换逻辑.
     * @param <T> T.
     * @param <R> R.
     * @return List<R>.
     */
    public static <T, R> List<R> transform(List<T> list, Function<T, R> function) {
        List<R> result = initList(list);

        if (list.isEmpty()) {
            return result;
        }

        list.forEach(element -> result.add(function.apply(element)));
        return result;
    }

    /**
     * list转换为Result.
     * @param list 源.
     * @param function 转换逻辑.
     * @param <T> T.
     * @param <R> R.
     * @return List<R>.
     */
    public static <T, R> Result<List<R>, Success<List<R>>> listToResult(List<T> list, Function<T, R> function) {
        return new ListSuccess<>(transform(list, function));
    }

    /**
     * 成功结果.
     * @param <T> T.
     * @return Result<T, Success<T>>.
     */
    public static <T> Result<T, Success<T>> success() {
        return success(null);
    }

    /**
     * 成功结果.
     * @param data 数据.
     * @param <T> T.
     * @return Result<T, Success<T>>.
     */
    @SuppressWarnings("all")
    public static <T> Result<T, Success<T>> success(T data) {
        if (data == null) {
            return new Success<>(null);
        }
        if (data instanceof List<?>) {
            return new ListSuccess(Unaware.castUnaware(data));
        }
        return new Success<>(data);
    }

    /**
     * 失败结果.
     * @param throwable 异常.
     * @param <T> T.
     * @return Result<T, Failure<T>>.
     */
    public static <T extends Throwable> Result<T, Failure<T>> failure(T throwable) {
        return new Failure<>(throwable);
    }

    /**
     * 失败结果.
     * @param state 状态码.
     * @param message 异常简短消息.
     * @param throwable 异常.
     * @param customizeMessage 自定义的消息.
     * @param originate 面向用户的消息.
     * @param <T> T.
     * @return Result<T, Failure<T>>.
     */
    public static <T extends Throwable> Result<T, Failure<T>> failure(int state, String message, T throwable, String customizeMessage, String originate) {
        return new Failure<>(state, message, throwable, customizeMessage, originate);
    }

    /**
     * 初始化.
     * @param list 源.
     * @param <T> T.
     * @param <R> R.
     * @return List<R>.
     */
    private static <T, R> List<R> initList(List<T> list) {
        if (list == null) {
            return Collections.emptyList();
        }

        List<R> result;

        if (list instanceof Page<?>) {
            Page<T> source = (Page<T>) list;
            Page<R> page = new Page<>();
            page.setPageNum(source.getPageNum());
            page.setPageSize(source.getPageSize());
            page.setTotal(source.getTotal());
            page.setPages(source.getPages());
            result = page;
        } else {
            result = new ArrayList<>();
        }
        return result;
    }

}
