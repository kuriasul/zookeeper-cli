package com.sikejava.tinytool.zookeeper.cli.service;

import com.google.common.base.Stopwatch;

import com.sikejava.tinytool.zookeeper.cli.command.RestoreCommand;
import com.sikejava.tinytool.zookeeper.cli.component.exception.BusinessException;
import com.sikejava.tinytool.zookeeper.cli.component.zookeeper.ZookeeperTemplate;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 恢复service
 *
 * @author skjv2014@163.com
 * @date 2022-06-27 00:00:45
 */
public class RestoreService {

    /**
     * 从文件中恢复数据
     *
     * @param zookeeperTemplate ZookeeperTemplate
     * @param restoreCommand 恢复命令
     * @author skjv2014@163.com
     * @date 2022-06-27 00:02:38
     */
    public void restoreDataFromFile(ZookeeperTemplate zookeeperTemplate, RestoreCommand restoreCommand) {
        System.out.println("info: 从文件中恢复数据的操作开始...");
        Stopwatch stopwatch = Stopwatch.createStarted();

        // --- 来源文件
        File sourceFile = new File(restoreCommand.getFilePath());
        if (sourceFile.isDirectory()) {
            throw new BusinessException("数据恢复来源必须是文件");
        }
        if (!sourceFile.exists()) {
            throw new BusinessException("数据恢复文件不存在");
        }

        // --- 恢复数据
        System.out.println("info: 开始读取恢复文件...");
        SAXReader saxReader = new SAXReader();
        Document document;
        try {
            document = saxReader.read(sourceFile);
        } catch (DocumentException e) {
            throw new BusinessException("读取恢复文件异常", e);
        }
        System.out.println("info: 读取恢复文件成功.");

        Element createsElement = document.getRootElement();
        List<Element> createElementList = createsElement.elements("create");

        System.out.println("info: 开始恢复数据...");
        if (Objects.nonNull(createElementList) && !createElementList.isEmpty()) {
            createElementList.forEach(createElement -> {
                String nodePath = createElement.elementText("nodePath");
                String nodeData = createElement.elementText("nodeData");

                if (StringUtils.isBlank(nodeData)) {
                    if (zookeeperTemplate.isNodeNotExist(nodePath)) {
                        zookeeperTemplate.createNode(nodePath);
                        System.out.println("info: " + nodePath + "节点恢复完成.");
                    } else {
                        System.out.println("info: " + nodePath + "节点已存在, 无需恢复.");
                    }
                } else {
                    if (zookeeperTemplate.isNodeNotExist(nodePath)) {
                        zookeeperTemplate.createNode(nodePath, nodeData);
                        System.out.println("info: " + nodePath + "节点恢复完成.");
                    } else {
                        if (restoreCommand.getOverrideData()) {
                            zookeeperTemplate.updateNodeData(nodePath, nodeData);
                            System.out.println("info: " + nodePath + "节点已存在, 刷新数据.");
                        } else {
                            System.out.println("info: " + nodePath + "节点已存在, 无需恢复.");
                        }
                    }
                }
            });
        }
        System.out.println("info: 恢复数据成功.");

        stopwatch.stop();
        System.out.println("info: 从文件中恢复数据的操作结束. 耗时: " + stopwatch.elapsed(TimeUnit.SECONDS) + "s.");
    }
}
