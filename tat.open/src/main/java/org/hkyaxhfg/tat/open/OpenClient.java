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
        return OpenCallable.executorService().submit(new OpenCallable<>(request, this::execute));
    }

    public static class OpenCallable<T extends Response> implements Callable<T> {

        private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

        private final Request<T> request;

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
