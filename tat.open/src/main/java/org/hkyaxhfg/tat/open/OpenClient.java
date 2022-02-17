package org.hkyaxhfg.tat.open;

import java.util.concurrent.*;
import java.util.function.Function;

/**
 * OpenClient: 开放客户端, 所有符合Request与Response的交互都可以通过OpenClient实现.
 *
 * @author: wjf
 * @date: 2022/2/17
 */
@FunctionalInterface
public interface OpenClient {

    /**
     * 执行.
     * @param request 请求.
     * @param <T> 响应.
     * @return Response.
     */
    <T extends Response> T execute(Request<T> request);

    /**
     * 异步执行.
     * @param request 请求.
     * @param <T> 响应.
     * @return Future<T>.
     * @throws ExecutionException 执行异常.
     * @throws InterruptedException 中断异常.
     */
    default <T extends Response> Future<T> asyncExecute(Request<T> request) throws ExecutionException, InterruptedException {
        return asyncExecute(new OpenCallable<>(request, this::execute), OpenCallable.executorService());
    }

    /**
     * 异步执行.
     * @param callable <C extends OpenCallable<T>>.
     * @param executorService 执行器服务.
     * @param <T> Response.
     * @param <C> <C extends OpenCallable<T>>.
     * @return Response.
     * @throws InterruptedException 中断异常.
     */
    default <T extends Response, C extends OpenCallable<T>> Future<T> asyncExecute(C callable, ExecutorService executorService) throws InterruptedException {
        return executorService.submit(callable);
    }

    /**
     * 默认的异步执行实现.
     * @param <T> Response.
     */
    public static class OpenCallable<T extends Response> implements Callable<T> {

        /**
         * 默认的线程执行服务.
         */
        private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

        /**
         * 请求.
         */
        private final Request<T> request;

        /**
         * 需要执行的操作.
         */
        private final Function<Request<T>, T> function;

        public OpenCallable(Request<T> request, Function<Request<T>, T> function) {
            this.request = request;
            this.function = function;
        }

        public static ExecutorService executorService() {
            return EXECUTOR_SERVICE;
        }

        @Override
        public T call() throws Exception {
            return this.function.apply(this.request);
        }
    }

}
