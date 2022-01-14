package org.hkyaxhfg.tat.snowflake;

import org.junit.Test;

/**
 * @author: wjf
 * @date: 2022/1/10
 */
public class SnowflakeIdWorkerTest {

    @Test
    public void nextId() {
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();
        System.out.println(snowflakeIdWorker.nextId());
    }

}
