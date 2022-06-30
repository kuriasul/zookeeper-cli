package com.sikejava.tinytool.zookeeper.cli.component.zookeeper;

import com.sikejava.tinytool.zookeeper.cli.component.log.DumpLogger;
import com.sikejava.tinytool.zookeeper.cli.component.utils.JsonUtil;

import org.apache.curator.framework.CuratorFramework;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * ZookeeperTemplate的实现
 *
 * @author skjv2014@163.com
 * @date 2022-06-09 10:06:24
 */
public final class ZookeeperTemplateImpl implements ZookeeperTemplate {
    private CuratorFramework zookeeperClient;

    public ZookeeperTemplateImpl(CuratorFramework zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
    }

    @Override
    public CuratorFramework getRawClient() {
        return this.zookeeperClient;
    }

    @Override
    public boolean isNodeExist(String nodePath) {
        try {
            return Objects.nonNull(zookeeperClient.checkExists().forPath(nodePath));
        } catch (Exception e) {
            throw new ZookeeperException("判断节点是否存在时发生异常", e);
        }
    }

    @Override
    public boolean isNodeNotExist(String nodePath) {
        return !isNodeExist(nodePath);
    }

    @Override
    public void createNode(String nodePath) {
        createNode(nodePath, "");
    }

    @Override
    public void createNodeIfNotExist(String nodePath) {
        if (isNodeNotExist(nodePath)) {
            createNode(nodePath);
        }
    }

    @Override
    public void createNode(String nodePath, String nodeData) {
        try {
            zookeeperClient.create().creatingParentsIfNeeded().forPath(nodePath, nodeData.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new ZookeeperException("创建节点时发生异常", e);
        }
    }

    @Override
    public void createNodeIfNotExist(String nodePath, String nodeData) {
        if (isNodeNotExist(nodePath)) {
            createNode(nodePath, nodeData);
        }
    }

    @Override
    public void deleteNode(String nodePath) {
        try {
            zookeeperClient.delete().guaranteed().deletingChildrenIfNeeded().forPath(nodePath);
        } catch (Exception e) {
            throw new ZookeeperException("删除节点时发生异常", e);
        }
    }

    @Override
    public void updateNodeData(String nodePath, String nodeData) {
        try {
            zookeeperClient.setData().forPath(nodePath, nodeData.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new ZookeeperException("更新节点数据时发生异常", e);
        }
    }

    @Override
    public void updateNodeData(String nodePath, Object obj) {
        try {
            zookeeperClient.setData().forPath(nodePath, JsonUtil.toJson(obj).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new ZookeeperException("更新节点数据时发生异常" ,e);
        }
    }

    @Override
    public String getNodeData(String nodePath) {
        try {
            byte[] nodeByteData = zookeeperClient.getData().forPath(nodePath);
            if (Objects.isNull(nodeByteData)) {
                return "";
            }
            return new String(nodeByteData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new ZookeeperException("获取节点数据时发生异常" ,e);
        }
    }

    @Override
    public <T> T getNodeData(String nodePath, Class<T> clazz) {
        String nodeData = getNodeData(nodePath);
        return JsonUtil.toObject(nodeData, clazz);
    }

    @Override
    public List<String> getChildNodeNameList(String parentNodePath) {
        try {
            return zookeeperClient.getChildren().watched().forPath(parentNodePath);
        } catch (Exception e) {
            throw new ZookeeperException("获取子节点名称列表时发生异常" ,e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            if (Objects.nonNull(zookeeperClient)) {
                zookeeperClient.close();
            }
            DumpLogger.debug("释放zookeeper客户端资源成功");
        } catch (Exception e) {
            DumpLogger.error("释放zookeeper客户端资源异常. 异常详情: ", e);
        }
    }
}
