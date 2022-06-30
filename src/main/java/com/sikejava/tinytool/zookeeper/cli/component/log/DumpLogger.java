package com.sikejava.tinytool.zookeeper.cli.component.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * dump日志器
 *
 * @author skjv2014@163.com
 * @date 2022-06-26 14:59:22
 */
public final class DumpLogger {
    private static final Logger logger = LoggerFactory.getLogger(DumpLogger.class);

    /**
     * 记录错误日志
     *
     * @param msg 信息
     * @return e 异常
     * @author skjv2014@163.com
     * @date 2022-06-28 10:06:34
     */
    public static void error(String msg, Throwable e) {
        logger.error(msg, e);
    }

    /**
     * 记录调试日志
     *
     * @param msg 信息
     * @author skjv2014@163.com
     * @date 2022-06-28 10:06:34
     */
    public static void debug(String msg) {
        logger.debug(msg);
    }

    private DumpLogger() {}
}
