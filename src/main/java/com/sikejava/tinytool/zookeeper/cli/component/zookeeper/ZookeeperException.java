package com.sikejava.tinytool.zookeeper.cli.component.zookeeper;

/**
 * zookeeper异常
 *
 * @author skjv2014@163.com
 * @date 2022-06-09 13:58:01
 */
public class ZookeeperException extends RuntimeException {

    public ZookeeperException(String message) {
        super(message);
    }

    public ZookeeperException(Throwable cause) {
        super(cause);
    }

    public ZookeeperException(String message, Throwable cause) {
        super(message, cause);
    }
}
