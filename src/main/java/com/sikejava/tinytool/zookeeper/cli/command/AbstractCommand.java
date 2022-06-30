package com.sikejava.tinytool.zookeeper.cli.command;

import com.sikejava.tinytool.zookeeper.cli.component.log.DumpLogger;
import com.sikejava.tinytool.zookeeper.cli.component.exception.BusinessException;
import com.sikejava.tinytool.zookeeper.cli.component.zookeeper.ZookeeperTemplate;
import com.sikejava.tinytool.zookeeper.cli.component.zookeeper.ZookeeperTemplateFactory;

import io.airlift.airline.Option;

/**
 * 抽象命令
 *
 * @author skjv2014@163.com
 * @date 2022-06-27 10:20:24
 */
public abstract class AbstractCommand implements Runnable {

    @Option(name = {"-s", "--server-address"}, title = "服务器地址", description = "zookeeper服务器地址(默认: 127.0.0.1:2181)")
    private String serverAddress = "127.0.0.1:2181";

    @Option(name = {"-n", "--namespace"}, title = "命名空间", description = "zookeeper命名空间(默认: 根命名空间)")
    private String namespace = "";

    @Override
    public void run() {
        System.out.println("info: 开始连接zookeeper...");
        try(ZookeeperTemplate zookeeperTemplate = getZookeeperTemplate()) {
            System.out.println("info: 连接zookeeper成功.");
            execute(zookeeperTemplate);
        } catch (Exception e) {
            String errorMsg = e instanceof BusinessException ? e.getMessage() : "系统异常, 请联系开发人员";

            DumpLogger.error(errorMsg, e);
            System.out.println("error: " + errorMsg);
        }
    }

    /**
     * 获取ZookeeperTemplate
     */
    private ZookeeperTemplate getZookeeperTemplate() {
        try {
            return ZookeeperTemplateFactory.builder()
                    .serverAddress(serverAddress)
                    .namespace(namespace)
                    .build();
        } catch (Exception e) {
            throw new BusinessException("连接zookeeper异常, 请检查");
        }
    }

    /**
     * 执行命令
     *
     * @author skjv2014@163.com
     * @date 2022-06-27 12:14:33
     */
    protected abstract void execute(ZookeeperTemplate zookeeperTemplate);
}
