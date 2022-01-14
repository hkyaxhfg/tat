package org.hkyaxhfg.tat.snowflake;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 利用ScheduledExecutorService实现高并发场景下System.currentTimeMillis()的性能问题的优化.
 *
 * @author: wjf
 * @date: 2022/1/10
 */
@SuppressWarnings("all")
public enum SystemClock {

    /**
     * 高性能实例.
     */
    HIGH_PERFORMANCE_INSTANCE(1, true),
    /**
     * 普通性能实例.
     */
    ORDINARY_PERFORMANCE_INSTANCE(1, false);

    private final long period;
    private final AtomicLong nowTime;
    private boolean started = false;
    private ScheduledExecutorService executorService;

    SystemClock(long period, boolean started) {
        this.period = period;
        this.started = started;
        this.nowTime = new AtomicLong(System.currentTimeMillis());
        initialize();
    }

    /**
     * 初始化调度的执行器服务.
     */
    public void initialize() {
        if (!started) {
            return;
        }

        this.executorService = new ScheduledThreadPoolExecutor(
                1,
                r -> {
                    Thread thread = new Thread(r, "system-clock");
                    thread.setDaemon(true);
                    return thread;
                }
        );

        executorService.scheduleAtFixedRate(
                () -> nowTime.set(System.currentTimeMillis()),
                this.period,
                this.period,
                TimeUnit.MILLISECONDS
        );
        Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
        started = true;
    }

    /**
     * 获取当前时间毫秒.
     *
     * @return 毫秒.
     */
    public long currentTimeMillis() {
        return started ? nowTime.get() : System.currentTimeMillis();
    }

    /**
     * executor服务的销毁.
     */
    public void destroy() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

}
