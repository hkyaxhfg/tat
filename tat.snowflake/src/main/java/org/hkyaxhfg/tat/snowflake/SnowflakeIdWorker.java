package org.hkyaxhfg.tat.snowflake;

import org.apache.commons.lang3.StringUtils;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.hkyaxhfg.tat.lang.util.TatException;
import org.slf4j.Logger;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 雪花算法的id生成器.
 *
 * 基于Twitter的Snowflake算法实现分布式高效有序ID生产黑科技(sequence)——升级版Snowflake.
 *
 * <br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * <br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * <br>
 * 1位标识: 由于long基本类型在Java中是带符号的, 最高位是符号位, 正数是0, 负数是1, 所以id一般是正数，最高位是0<br>
 * <br>
 * 41位时间截(毫秒级): 注意, 41位时间截不是存储当前时间的时间截, 而是存储时间截的差值(当前时间截 - 开始时间截)得到的值,
 * 这里的的开始时间截, 一般是我们的id生成器开始使用的时间, 由我们程序来指定的(如下START_TIME属性). 41位的时间截可以使
 * 用69年, 年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * <br>
 * 10位的数据机器位: 可以部署在1024个节点, 包括5位dataCenterId和5位workerId.<br>
 * <br>
 * 12位序列: 毫秒内的计数, 12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号.<br>
 * <br>
 * <br>
 * 总的加起来刚好64位, 为一个Long型.<br>
 * SnowFlake的优点是, 整体上按照时间自增排序, 并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分),并且效率较高,
 * 经测试, SnowFlake每秒能够产生26万ID左右.
 * <p>
 * <p>
 * 特性:<br>
 * 1.支持自定义允许时间回拨的范围.<p>
 * 2.解决跨毫秒起始值每次为0开始的情况(避免末尾必定为偶数，而不便于取余使用问题).<p>
 * 3.解决高并发场景中获取时间戳性能问题.<p>
 * 4.支撑根据IP末尾数据作为workerId.<p>
 * 5.时间回拨方案思考: 1024个节点中分配10个点作为时间回拨序号(连续10次时间回拨的概率较小).<p>
 *
 * @author: wjf
 * @date: 2022/1/10
 */
public class SnowflakeIdWorker {

    private static final Logger logger = LoggerGenerator.logger(SnowflakeIdWorker.class);

    /**
     * 起始时间戳. (默认为 2022-01-01 00:00:00)
     **/
    private static final long START_TIME = 1640966400000L;
    /**
     * dataCenterId(数据中心id)占用的位数: 5.
     **/
    private static final long DATA_CENTER_ID_BITS = 5L;
    /**
     * workerId(工作者id)占用的位数: 5.
     **/
    private static final long WORKER_ID_BITS = 5L;
    /**
     * 序列号占用的位数: 12 (表示只允许workId的范围为: 0-4095).
     **/
    private static final long SEQUENCE_BITS = 12L;
    /**
     * workerId可以使用范围: 0-255.
     **/
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    /**
     * dataCenterId可以使用范围: 0-3.
     **/
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);
    /**
     * 工作者id转移.
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    /**
     * 数据中心id转移.
     */
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    /**
     * 时间戳左移.
      */
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + WORKER_ID_BITS;
    /**
     * 用mask防止溢出: 位与运算保证计算的结果范围始终是 0-4095.
     **/
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    /**
     * 工作者id.
     */
    private final long workerId;
    /**
     * 数据中心id.
     */
    private final long dataCenterId;
    /**
     * true表示解决高并发下获取时间戳的性能问题.
     */
    private final boolean clock;
    /**
     * 允许时间回拨的毫秒量, 建议5ms.
     */
    private final long timeOffset;
    /**
     * true表示使用毫秒内的随机序列(超过范围则取余).
     */
    private final boolean randomSequence;
    /**
     * 线程局部随机生产器.
     */
    private final ThreadLocalRandom tlr = ThreadLocalRandom.current();
    /**
     * 序列号.
     */
    private long sequence = 0L;
    /**
     * 最后一个时间戳.
     */
    private long lastTimestamp = -1L;
    /**
     * 机器的ip.
     */
    private InetAddress inetAddress;

    /**
     * 无参构造, 默认根据ip地址获取.
     */
    public SnowflakeIdWorker() {
        this(null);
    }

    /**
     * 一个参数的构造方法, 需要传入{@link InetAddress}.
     * @param inetAddress inetAddress.
     */
    public SnowflakeIdWorker(InetAddress inetAddress) {
        this(inetAddress, true, 5L, true);
    }

    /**
     * 两个参数的构造方法, 需要传入数据中心id.
     * @param dataCenterId 数据中心id.
     * @param workerId 工作者id.
     */
    public SnowflakeIdWorker(long dataCenterId, long workerId) {
        this(dataCenterId, workerId, true, 5L, true);
    }

    /**
     * 基于Snowflake创建分布式ID生成器.
     *
     * @param dataCenterId   数据中心ID, 数据范围为0~255.
     * @param workerId       工作机器ID, 数据范围为0~3.
     * @param clock          true表示解决高并发下获取时间戳的性能问题.
     * @param timeOffset     允许时间回拨的毫秒量, 建议5ms.
     * @param randomSequence true表示使用毫秒内的随机序列(超过范围则取余).
     */
    private SnowflakeIdWorker(long dataCenterId, long workerId, boolean clock, long timeOffset, boolean randomSequence) {
        this.dataCenterId = this.checkDataCenterId(dataCenterId);
        this.workerId = this.checkWorkerId(workerId);
        this.clock = clock;
        this.timeOffset = timeOffset;
        this.randomSequence = randomSequence;
    }

    /**
     * 基于Snowflake创建分布式ID生成器.
     * @param inetAddress    ip.
     * @param clock          true表示解决高并发下获取时间戳的性能问题.
     * @param timeOffset     允许时间回拨的毫秒量, 建议5ms.
     * @param randomSequence true表示使用毫秒内的随机序列(超过范围则取余).
     */
    public SnowflakeIdWorker(InetAddress inetAddress, boolean clock, long timeOffset, boolean randomSequence) {
        this.inetAddress = inetAddress;
        this.dataCenterId = this.checkDataCenterId(this.getDatacenterId());
        this.workerId = this.checkWorkerId(this.getWorkerId());
        this.clock = clock;
        this.timeOffset = timeOffset;
        this.randomSequence = randomSequence;
    }

    /**
     * 获取ID.
     *
     * @return id.
     */
    @SuppressWarnings("all")
    public synchronized Long nextId() {
        long currentTimestamp = this.timeGen();

        // 闰秒: 如果当前时间小于上一次ID生成的时间戳, 说明系统时钟回退过, 这个时候应当抛出异常
        if (currentTimestamp < lastTimestamp) {
            // 校验时间偏移回拨量
            long offset = lastTimestamp - currentTimestamp;
            if (offset > timeOffset) {
                throw TatException.newEx("Clock moved backwards, refusing to generate id for [{}ms]", offset);
            }

            try {
                // 时间回退timeOffset毫秒内, 则允许等待2倍的偏移量后重新获取, 解决小范围的时间回拨问题
                this.wait(offset << 1);
            } catch (Exception e) {
                throw TatException.newEx(e.toString());
            }
            // 再次获取
            currentTimestamp = this.timeGen();
            // 再次校验
            if (currentTimestamp < lastTimestamp) {
                throw TatException.newEx("Clock moved backwards, refusing to generate id for [{}ms]", offset);
            }
        }

        // 同一毫秒内序列直接自增
        if (lastTimestamp == currentTimestamp) {
            // randomSequence为true表示随机生成允许范围内的序列起始值并取余数, 否则毫秒内起始值为0L开始自增
            long tempSequence = sequence + 1;
            if (randomSequence && tempSequence > SEQUENCE_MASK) {
                tempSequence = tempSequence % SEQUENCE_MASK;
            }

            // 通过位与运算保证计算的结果范围始终是 0-4095
            sequence = tempSequence & SEQUENCE_MASK;
            if (sequence == 0) {
                currentTimestamp = this.tilNextMillis(lastTimestamp);
            }
        } else {
            // randomSequence为true表示随机生成允许范围内的序列起始值, 否则毫秒内起始值为0L开始自增
            sequence = randomSequence ? tlr.nextLong(SEQUENCE_MASK + 1) : 0L;
        }

        lastTimestamp = currentTimestamp;
        long currentOffsetTime = currentTimestamp - START_TIME;

        /*
         * 1.左移运算是为了将数值移动到对应的段(41, 5, 5, 12那段因为本来就在最右, 因此不用左移)
         * 2.然后对每个左移后的值(la, lb, lc, sequence)做位或运算, 是为了把各个短的数据合并起来, 合并成一个二进制数
         * 3.最后转换成10进制, 就是最终生成的id
         */
        return (currentOffsetTime << TIMESTAMP_LEFT_SHIFT) |
                // 数据中心位
                (dataCenterId << DATA_CENTER_ID_SHIFT) |
                // 工作ID位
                (workerId << WORKER_ID_SHIFT) |
                // 毫秒序列化位
                sequence;
    }

    /**
     * 根据ip获取数据中心id.
     * @return 数据中心id.
     */
    protected long getDatacenterId() {
        long id = 0L;
        try {
            // 获取机器ip
            if (null == this.inetAddress) {
                this.inetAddress = InetAddress.getLocalHost();
            }
            NetworkInterface network = NetworkInterface.getByInetAddress(this.inetAddress);
            if (null == network) {
                id = 1L;
            } else {
                // 否则获取mac地址
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    id = ((0x000000FF & (long) mac[mac.length - 2]) | (0x0000FF00 & (((long) mac[mac.length - 1]) << 8))) >> 6;
                    id = id % (MAX_DATA_CENTER_ID + 1);
                }
            }
        } catch (Exception e) {
            logger.warn(" getDatacenterId: {} ", e.toString());
        }
        return id;
    }

    /**
     * 获取workerId.
     * @return 工作者id.
     */
    protected long getWorkerId() {
        StringBuilder macPid = new StringBuilder();
        macPid.append(this.dataCenterId);
        // 获取 Java 虚拟机的运行时系统的托管 bean
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (StringUtils.isNotBlank(name)) {
            // 获取jvmPid
            macPid.append(name.split("@")[0]);
        }
        // MAC + PID 的 hashcode, 获取16个低位
        return (macPid.toString().hashCode() & 0xffff) % (MAX_WORKER_ID + 1);
    }

    /**
     * 校验dataCenterId.
     * @param dataCenterId dataCenterId.
     * @return  dataCenterId dataCenterId.
     */
    protected long checkDataCenterId(long dataCenterId) {
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException("Data Center Id can't be greater than " + MAX_DATA_CENTER_ID + " or less than 0");
        }
        return dataCenterId;
    }

    /**
     * 校验workerId.
     * @param workerId workerId.
     * @return  workerId workerId.
     */
    protected long checkWorkerId(long workerId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("Worker Id can't be greater than " + MAX_WORKER_ID + " or less than 0");
        }
        return workerId;
    }

    /**
     * 保证返回的毫秒数在参数之后(阻塞到下一个毫秒，直到获得新的时间戳)——CAS.
     *
     * @param lastTimestamp last timestamp.
     * @return next millis.
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            // 如果发现时间回拨，则自动重新获取(可能会处于无限循环中)
            timestamp = this.timeGen();
        }

        return timestamp;
    }

    /**
     * 获得系统当前毫秒时间戳.
     *
     * @return timestamp 毫秒时间戳.
     */
    private long timeGen() {
        return clock ? SystemClock.HIGH_PERFORMANCE_INSTANCE.currentTimeMillis() : System.currentTimeMillis();
    }
}
