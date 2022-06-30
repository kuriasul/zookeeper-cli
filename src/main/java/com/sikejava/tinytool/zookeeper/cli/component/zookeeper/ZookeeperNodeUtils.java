package com.sikejava.tinytool.zookeeper.cli.component.zookeeper;

import com.sikejava.tinytool.zookeeper.cli.component.validator.Assert;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * zookeeper节点工具类
 *
 * @author skjv2014@163.com
 * @date 2022-06-09 15:25:38
 */
public final class ZookeeperNodeUtils {

    /**
     * 获取节点路径
     *
     * @param parentNodePath 父节点路径
     * @param currentNodeName 当前节点名称
     * @return String 节点路径
     * @author skjv2014@163.com
     * @date 2022-06-27 00:07:19
     */
    public static String getNodePathByParent(String parentNodePath, String currentNodeName) {
        return Objects.equals("/", parentNodePath) ? parentNodePath + currentNodeName : parentNodePath + "/" + currentNodeName;
    }

    /**
     * 获取节点路径
     *
     * @param nodeNameArray 节点名称数组
     * @return String 节点路径
     * @author skjv2014@163.com
     * @date 2022-06-09 15:31:31
     */
    public static String getNodePath(String ... nodeNameArray) {
        Assert.notEmpty(nodeNameArray, "节点名称列表不能为空");

        StringBuilder nodePathBuilder = new StringBuilder();
        for (String nodeName : nodeNameArray) {
            nodePathBuilder.append("/").append(nodeName);
        }
        return nodePathBuilder.toString();
    }

    /**
     * 根据节点路径获取节点名称
     *
     * @param nodePath 节点路径
     * @return String 节点名称
     * @author skjv2014@163.com
     * @date 2022-06-16 13:33:20
     */
    public static String getNodeName(String nodePath) {
        if (StringUtils.isBlank(nodePath)) {
            return "";
        }
        int slashIndex = nodePath.lastIndexOf("/");
        if (slashIndex == -1) {
            throw new ZookeeperException("zookeeper路径错误");
        }
        return nodePath.substring(slashIndex + 1);
    }

    private ZookeeperNodeUtils() {}
}
