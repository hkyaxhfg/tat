package org.hkyaxhfg.tat.lang.util.thread;

import org.apache.commons.collections4.CollectionUtils;
import org.hkyaxhfg.tat.lang.util.TatException;
import org.hkyaxhfg.tat.lang.util.function.ListFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * 一个多任务的执行器, 可以将一个大任务拆分成多个小任务执行, 最终合并得到一个完整的结果.
 *
 * @author: wjf
 * @date: 2021/11/1 17:44
 */
@SuppressWarnings("all")
public class ListForkJoinTask<T, R> {

    /**
     * 每个子任务所处理的最大元素个数.
     */
    private int size;

    /**
     * 所要处理的集合.
     */
    private List<T> list;

    /**
     * 超时时间。
     */
    private int timeout = 3;

    /**
     * 时间单位。
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * 构造方法, 需要传入四个参数, 参数都不能为空.
     * @param size 每个小任务需要处理的数据的最大长度.
     * @param list 源list.
     * @param timeout 超时时间.
     * @param timeUnit 时间单位.
     */
    public ListForkJoinTask(int size, List<T> list, int timeout, TimeUnit timeUnit) {
        this.size = size;
        this.list = list;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    /**
     * 执行, 需要传入一个函数式接口.
     * @param function 函数式接口.
     * @return List<R>.
     */
    public List<R> execute(ListFunction<T, R> function) {
        ForkJoinPool forkJoinPool = null;
        try {
            forkJoinPool = new ForkJoinPool();
            ForkJoinTask<List<R>> joinTask = forkJoinPool.submit(new ListForkJoinSubTask<>(this.size, this.list, function));

            forkJoinPool.awaitTermination(this.timeout, this.timeUnit);

            forkJoinPool.shutdown();
            return joinTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw TatException.newEx(e.toString());
        } finally {
            if (forkJoinPool != null && !forkJoinPool.isShutdown()) {
                forkJoinPool.shutdown();
            }
        }
    }

    /**
     * ListForkJoinSubTask.
     * @param <T> T.
     * @param <R> R.
     */
    private static class ListForkJoinSubTask<T, R> extends RecursiveTask<List<R>> {

        /**
         * 任务所处理的最大元素个数.
         */
        private final int size;

        /**
         * 当前任务所要处理的集合.
         */
        private final List<T> currentList;

        /**
         * 对每个子集合的处理, 由开发者自行实现.
         */
        private final ListFunction<T, R> function;

        /**
         * 构造方法, 需要传入三个参数.
         * @param size 任务所处理的最大元素个数.
         * @param currentList 当前任务所要处理的集合.
         * @param function 对每个子集合的处理, 由开发者自行实现.
         */
        public ListForkJoinSubTask(int size, List<T> currentList, ListFunction<T, R> function) {
            Objects.requireNonNull(currentList);
            Objects.requireNonNull(function);
            this.size = size;
            this.currentList = currentList;
            this.function = function;
        }

        @Override
        protected List<R> compute() {
            if (this.currentList.isEmpty()) {
                return Collections.emptyList();
            }
            int size = this.currentList.size();
            if (size <= this.size) {
                return function.apply(this.currentList);
            }

            int index = size / 2;
            ListForkJoinSubTask<T, R> leftTask = new ListForkJoinSubTask<>(this.size, this.currentList.subList(0, index), this.function);
            ListForkJoinSubTask<T, R> rightTask = new ListForkJoinSubTask<>(this.size, this.currentList.subList(index, size), this.function);
            leftTask.fork();
            rightTask.fork();
            List<R> leftList = leftTask.join();
            List<R> rightList = rightTask.join();
            List<R> result = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(leftList)) {
                result.addAll(leftList);
            }
            if (CollectionUtils.isNotEmpty(rightList)) {
                result.addAll(rightList);
            }
            return result;
        }

    }

}
