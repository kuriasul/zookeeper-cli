package com.sikejava.tinytool.zookeeper.cli.service;

import com.google.common.base.Stopwatch;

import com.sikejava.tinytool.zookeeper.cli.command.SetDataCommand;
import com.sikejava.tinytool.zookeeper.cli.component.zookeeper.ZookeeperNodeUtils;
import com.sikejava.tinytool.zookeeper.cli.component.zookeeper.ZookeeperTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 设置数据service
 *
 * @author skjv2014@163.com
 * @date 2022-06-26 23:15:26
 */
public class SetDataService {

    /**
     * 设置数据
     *
     * @param zookeeperTemplate ZookeeperTemplate
     * @param setDataCommand 设置数据命令
     * @author skjv2014@163.com
     * @date 2022-06-27 12:34:00
     */
    public void setData(ZookeeperTemplate zookeeperTemplate, SetDataCommand setDataCommand) {
        System.out.println("info: 设置数据的操作开始...");
        Stopwatch stopwatch = Stopwatch.createStarted();

        // 处理子节点
        System.out.println("info: 开始设置数据...");
        handleChildData("/", zookeeperTemplate, setDataCommand);
        System.out.println("info: 设置数据成功.");

        stopwatch.stop();
        System.out.println("info: 设置数据的操作结束. 耗时: " + stopwatch.elapsed(TimeUnit.SECONDS) + "s.");
    }

    /**
     * 处理子节点数据
     */
    private void handleChildData(String parentNodePath, ZookeeperTemplate zookeeperTemplate, SetDataCommand setDataCommand) {
        List<String> childNodeNameList = zookeeperTemplate.getChildNodeNameList(parentNodePath);

        if (Objects.isNull(childNodeNameList) || childNodeNameList.isEmpty()) {
            return;
        }
        for (String childNodeName : childNodeNameList) {
            String childNodePath = ZookeeperNodeUtils.getNodePathByParent(parentNodePath, childNodeName);

            if (childNodePath.matches(setDataCommand.getNodePattern())) {
                zookeeperTemplate.updateNodeData(childNodePath, setDataCommand.getNodeData());
                System.out.println("info: " + childNodePath + "节点设置数据完成.");
            }

            handleChildData(childNodePath, zookeeperTemplate, setDataCommand);
        }
    }
}
