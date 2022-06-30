package com.sikejava.tinytool.zookeeper.cli.component.zookeeper;

import org.apache.curator.framework.CuratorFramework;

import java.io.Closeable;
import java.util.List;

/**
 * zookeeper template
 *
 * @author skjv2014@163.com
 * @date 2022-06-09 14:53:21
 */
public interface ZookeeperTemplate extends AutoCloseable {

    /**
     * 获取原始客户端
     *
     * @return CuratorFramework 原始客户端
     * @author skjv2014@163.com
     * @date 2022-06-09 15:45:07
     */
    CuratorFramework getRawClient();

    /**
     * 判断node是否存在
     *
     * @param nodePath node的path
     * @return boolean true-存在, false-不存在
     * @author skjv2014@163.com
     * @date 2022-06-09 13:45:56
     */
    boolean isNodeExist(String nodePath);

    /**
     * 判断node是否不存在
     *
     * @param nodePath node的path
     * @return boolean true-不存在, false-存在
     * @author skjv2014@163.com
     * @date 2022-06-09 13:45:56
     */
    boolean isNodeNotExist(String nodePath);

    /**
     * 创建node
     *
     * @param nodePath node的path
     * @author skjv2014@163.com
     * @date 2022-06-09 13:51:57
     */
    void createNode(String nodePath);

    /**
     * 如果节点不存在时就创建
     *
     * @param nodePath node的path
     * @return int 0-存在, 1-创建了
     * @author skjv2014@163.com
     * @date 2022-06-10 15:19:29
     */
    void createNodeIfNotExist(String nodePath);

    /**
     * 创建node
     *
     * @param nodePath node的path
     * @param nodeData node的data
     * @author skjv2014@163.com
     * @date 2022-06-09 13:53:53
     */
    void createNode(String nodePath, String nodeData);

    /**
     * 如果节点不存在时就创建
     *
     * @param nodePath node的path
     * @param nodeData node的data
     * @author skjv2014@163.com
     * @date 2022-06-09 13:53:53
     */
    void createNodeIfNotExist(String nodePath, String nodeData);

    /**
     * 删除node
     *
     * @param nodePath node的path
     * @author skjv2014@163.com
     * @date 2022-06-09 13:56:12
     */
    void deleteNode(String nodePath);

    /**
     * 更新节点数据
     *
     * @param nodePath node的path
     * @param nodeData node的data
     * @author skjv2014@163.com
     * @date 2022-06-09 14:07:37
     */
    void updateNodeData(String nodePath, String nodeData);

    /**
     * 更新节点数据
     *
     * @param nodePath node的path
     * @param obj node的data
     * @author skjv2014@163.com
     * @date 2022-06-09 14:07:37
     */
    void updateNodeData(String nodePath, Object obj);

    /**
     * 获取节点数据文本
     *
     * @param nodePath node的path
     * @return 节点数据
     * @author skjv2014@163.com
     * @date 2022-06-17 15:10:19
     */
    String getNodeData(String nodePath);

    /**
     * 获取节点数据对象
     *
     * @param nodePath node的path
     * @param clazz 节点数据对象类
     * @return T 节点数据对象
     * @author skjv2014@163.com
     * @date 2022-06-17 15:10:19
     */
    <T> T getNodeData(String nodePath, Class<T> clazz);

    /**
     * 获取子节点名称列表
     *
     * @param parentNodePath 父节点路径
     * @return List<String> 子节点名称列表
     * @author skjv2014@163.com
     * @date 2022-06-17 15:10:19
     */
    List<String> getChildNodeNameList(String parentNodePath);
}
