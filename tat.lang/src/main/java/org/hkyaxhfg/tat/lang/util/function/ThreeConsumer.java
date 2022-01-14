package org.hkyaxhfg.tat.lang.util.function;

/**
 * 三个参数的consumer.
 *
 * @param <P1> 参数1.
 * @param <P2> 参数2.
 * @param <P3> 参数3.
 *
 * @author: wjf
 * @date: 2022/1/8
 */
public interface ThreeConsumer<P1, P2, P3> {

    /**
     * 消费.
     * @param p1 参数1.
     * @param p2 参数2.
     * @param p3 参数3.
     */
    void accept(P1 p1, P2 p2, P3 p3);

}
