package com.sikejava.tinytool.zookeeper.cli.component.zookeeper;

import com.sikejava.tinytool.zookeeper.cli.component.exception.BusinessException;

import org.apache.commons.lang3.StringUtils;

/**
 * zookeeper指令工厂
 *
 * @author skjv2014@163.com
 * @date 2022-06-26 23:32:19
 */
public final class ZookeeperInstructionFactory {

    /**
     * 获取创建节点指令
     *
     * @param nodePath 节点路径
     * @return String 创建节点指令
     * @author skjv2014@163.com
     * @date 2022-06-26 23:51:20
     */
    public static String getCreateNodeInstruction(String nodePath) {
        if (StringUtils.isBlank(nodePath)) {
            throw new BusinessException("节点路径为空");
        }

        return "create@@@" + nodePath;
    }

    /**
     * 获取设置节点数据指令
     *
     * @param nodePath 节点路径
     * @param nodeData 节点数据
     * @return String 设置节点数据指令
     * @author skjv2014@163.com
     * @date 2022-06-26 23:51:20
     */
    public static String getSetNodeDataInstruction(String nodePath, String nodeData) {
        if (StringUtils.isBlank(nodePath)) {
            throw new BusinessException("节点路径为空");
        }
        if (StringUtils.isBlank(nodeData)) {
            throw new BusinessException("节点数据为空");
        }

        return "set@@@" + nodePath + "@@@" + nodeData;
    }

    private ZookeeperInstructionFactory() {}
}
