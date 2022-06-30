package com.sikejava.tinytool.zookeeper.cli.component.zookeeper;

import com.sikejava.tinytool.zookeeper.cli.component.validator.Assert;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * ZookeeperTemplate工厂
 *
 * @author skjv2014@163.com
 * @date 2022-06-10 10:38:53
 */
public final class ZookeeperTemplateFactory {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private static final String DEFAULT_ROOT_NAMESPACE = "";

        private static final int DEFAULT_BASE_SLEEP_TIME_MS = 1000;

        private static final int DEFAULT_MAX_RETRIES = 3;

        private static final int DEFAULT_MAX_SLEEP_TIME_MS = 3000;

        /**
         * 服务器地址
         */
        private String serverAddress;
        /**
         * 根命名空间
         */
        private String namespace;
        /**
         * zookeeper重试基础间隔时间
         * 单位: ms
         */
        private Integer baseSleepTimeMs;
        /**
         * zookeeper重试最大次数
         */
        private Integer maxRetries;
        /**
         * zookeeper重试最大间隔时间
         * 单位: ms
         */
        private Integer maxSleepTimeMs;

        public Builder serverAddress(String serverAddress) {
            this.serverAddress = serverAddress;
            return this;
        }

        public Builder namespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public Builder baseSleepTimeMs(int baseSleepTimeMs) {
            this.baseSleepTimeMs = baseSleepTimeMs;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder maxSleepTimeMs(int maxSleepTimeMs) {
            this.maxSleepTimeMs = maxSleepTimeMs;
            return this;
        }

        public ZookeeperTemplate build() {
            Assert.hasText(this.serverAddress, "zookeeper服务器地址不能为空");

            String namespace = Optional.ofNullable(this.namespace).orElse(DEFAULT_ROOT_NAMESPACE);
            int baseSleepTimeMillisecond = Optional.ofNullable(this.baseSleepTimeMs).orElse(DEFAULT_BASE_SLEEP_TIME_MS);
            int maxRetryTimes = Optional.ofNullable(this.maxRetries).orElse(DEFAULT_MAX_RETRIES);
            int maxSleepTimeMillisecond = Optional.ofNullable(this.maxSleepTimeMs).orElse(DEFAULT_MAX_SLEEP_TIME_MS);

            CuratorFramework zookeeperClient = CuratorFrameworkFactory.builder()
                    .connectString(this.serverAddress)
                    .namespace(namespace)
                    .retryPolicy(new ExponentialBackoffRetry(baseSleepTimeMillisecond, maxRetryTimes, maxSleepTimeMillisecond))
                    .build();
            zookeeperClient.start();

            boolean isConnected;
            try {
                isConnected = zookeeperClient.blockUntilConnected(8000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new ZookeeperException("连接zookeeper异常", e);
            }
            if (!isConnected) {
                throw new ZookeeperException("未连接上zookeeper, 请检查");
            }

            return new ZookeeperTemplateImpl(zookeeperClient);
        }
    }

    private ZookeeperTemplateFactory() {}
}
