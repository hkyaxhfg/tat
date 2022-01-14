package org.hkyaxhfg.tat.lang.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志记录器生成器.
 *
 * @author: wjf
 * @date: 2022/1/6
 */
public class LoggerGenerator {

    private LoggerGenerator() {}

    /**
     * 获取日志记录器.
     * @param clazz clazz.
     * @return 日志记录器.
     */
    public static Logger logger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * 获取日志记录器.
     * @param loggerName loggerName.
     * @return 日志记录器.
     */
    public static Logger logger(String loggerName) {
        return LoggerFactory.getLogger(loggerName);
    }

}
