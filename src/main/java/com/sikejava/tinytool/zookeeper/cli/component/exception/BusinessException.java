package com.sikejava.tinytool.zookeeper.cli.component.exception;

/**
 * 业务异常
 *
 * @author skjv2014@163.com
 * @date 2022-06-26 14:00:17
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
