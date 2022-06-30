package com.sikejava.tinytool.zookeeper.cli.service;

import com.google.common.base.Stopwatch;

import com.sikejava.tinytool.zookeeper.cli.command.BackupCommand;
import com.sikejava.tinytool.zookeeper.cli.component.exception.BusinessException;
import com.sikejava.tinytool.zookeeper.cli.component.log.DumpLogger;
import com.sikejava.tinytool.zookeeper.cli.component.zookeeper.ZookeeperNodeUtils;
import com.sikejava.tinytool.zookeeper.cli.component.zookeeper.ZookeeperTemplate;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 备份service
 *
 * @author skjv2014@163.com
 * @date 2022-06-26 23:15:26
 */
public class BackupService {

    /**
     * 备份数据到文件
     *
     * @param zookeeperTemplate ZookeeperTemplate
     * @param backupCommand 备份命令
     * @author skjv2014@163.com
     * @date 2022-06-26 23:21:14
     */
    public void backupData2File(ZookeeperTemplate zookeeperTemplate, BackupCommand backupCommand) {
        System.out.println("info: 数据备份到文件的操作开始...");
        Stopwatch stopwatch = Stopwatch.createStarted();

        // --- 目标文件
        File targetFile = new File(backupCommand.getFilePath());
        if (targetFile.isDirectory()) {
            throw new BusinessException("数据备份目标必须是文件");
        }
        if (targetFile.exists()) {
            System.out.println("info: 数据备份目标文件已存在, 无需创建");
        } else {
            System.out.println("info: 数据备份目标文件不存在, 需要创建");
            try {
                File parentFile = targetFile.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                targetFile.createNewFile();
                System.out.println("info: 创建数据备份目标文件成功");
            } catch (IOException e) {
                throw new BusinessException("创建数据备份目标文件失败", e);
            }
        }

        // --- 备份数据
        // 根节点
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement("creates");
        // 处理子节点
        System.out.println("info: 开始备份数据...");
        handleChildElement("/", zookeeperTemplate, rootElement, backupCommand);
        System.out.println("info: 备份数据成功.");
        // 写到文件中
        System.out.println("info: 开始写入文件...");
        XMLWriter writer = null;
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            writer = new XMLWriter(new OutputStreamWriter(Files.newOutputStream(targetFile.toPath()), StandardCharsets.UTF_8), format);
            writer.write(document);
        } catch (IOException e) {
            throw new BusinessException("往文件中写入备份数据异常", e);
        } finally {
            try {
                if(Objects.nonNull(writer)) {
                    writer.close();
                }
            } catch (IOException e) {
                DumpLogger.error("释放writer资源异常", e);
            }
        }
        System.out.println("info: 写入文件成功.");

        stopwatch.stop();
        System.out.println("info: 数据备份到文件的操作结束. 耗时: " + stopwatch.elapsed(TimeUnit.SECONDS) + "s.");
    }

    /**
     * 备份数据到文件
     */
    private void handleChildElement(String parentNodePath, ZookeeperTemplate zookeeperTemplate, Element rootElement, BackupCommand backupCommand) {
        List<String> childNodeNameList = zookeeperTemplate.getChildNodeNameList(parentNodePath);

        if (Objects.isNull(childNodeNameList) || childNodeNameList.isEmpty()) {
            return;
        }
        for (String childNodeName : childNodeNameList) {
            String childNodePath = ZookeeperNodeUtils.getNodePathByParent(parentNodePath, childNodeName);
            String childNodeData = zookeeperTemplate.getNodeData(childNodePath);

            if (StringUtils.isNotBlank(backupCommand.getExcludePattern()) && childNodePath.matches(backupCommand.getExcludePattern())) {
                System.out.println("info: " + childNodePath + "节点备份跳过.");
                continue;
            }

            // 特殊字符
            if (Objects.equals("\u0007", childNodeData)) {
                childNodeData = "";
            }

            // create元素
            Element createElement = rootElement.addElement("create");
            // create元素下的节点路径元素
            Element nodePathElement = createElement.addElement("nodePath");
            nodePathElement.setText(childNodePath);
            // create元素下的节点数据元素
            Element nodeDataElement = createElement.addElement("nodeData");
            nodeDataElement.setText(StringUtils.isNotBlank(childNodeData) ? childNodeData : "");

            System.out.println("info: " + childNodePath + "节点备份完成.");

            handleChildElement(childNodePath, zookeeperTemplate, rootElement, backupCommand);
        }
    }
}
