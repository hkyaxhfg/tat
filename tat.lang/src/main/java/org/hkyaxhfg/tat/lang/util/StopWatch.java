package org.hkyaxhfg.tat.lang.util;

/**
 * 秒表, 记录时间使用.
 *
 * @author: wjf
 * @date: 2022/1/6
 */
public class StopWatch {

    /**
     * 开始时间.
     */
    private long startTimeMillis;

    /**
     * 结束时间.
     */
    private long stopTimeMillis;

    /**
     * 构造方法.
     */
    public StopWatch() {}

    /**
     * 开始.
     */
    public void start() {
        this.startTimeMillis = System.currentTimeMillis();
    }

    /**
     * 结束.
     */
    public void stop() {
        this.stopTimeMillis = System.currentTimeMillis();
    }

    /**
     * 时间差值-毫秒值.
     * @return 毫秒值.
     */
    public long timeDifferenceMillisecond() {
        return timeDifference();
    }

    /**
     * 时间差值-秒值.
     * @return 秒值.
     */
    public long timeDifferenceSecond() {
        return timeDifference() / 1000;
    }

    /**
     * 时间差值-毫秒值.
     * @return 毫秒值.
     */
    private long timeDifference() {
        return this.stopTimeMillis - this.startTimeMillis;
    }

}
