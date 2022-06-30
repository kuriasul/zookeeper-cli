package com.sikejava.tinytool.zookeeper.cli.command;

import com.sikejava.tinytool.zookeeper.cli.service.RestoreService;
import com.sikejava.tinytool.zookeeper.cli.component.zookeeper.ZookeeperTemplate;

import org.apache.commons.lang3.StringUtils;

import io.airlift.airline.Command;
import io.airlift.airline.Option;

/**
 * 恢复命令
 *
 * @author skjv2014@163.com
 * @date 2022-06-27 10:20:24
 */
@Command(name = "restore", description = "恢复数据")
public class RestoreCommand extends AbstractCommand {

    @Option(name = {"-f", "--file"}, title = "文件路径", description = "数据备份文件的路径", required = true)
    public String filePath;

    @Option(name = {"-o", "--override-data"}, description = "当节点存在时覆盖数据(默认: 覆盖)")
    public Boolean overrideData = true;

    public String getFilePath() {
        return filePath;
    }

    public Boolean getOverrideData() {
        return overrideData;
    }

    @Override
    public void execute(ZookeeperTemplate zookeeperTemplate) {
        // 处理参数
        if (StringUtils.isBlank(filePath)) {
            System.out.println("error: 文件路径必须有值");
            return;
        }
        if (!filePath.endsWith(".xml")) {
            System.out.println("error: 文件后缀名必须是.xml");
            return;
        }

        // 恢复数据
        RestoreService restoreService = new RestoreService();
        restoreService.restoreDataFromFile(zookeeperTemplate, this);
    }
}
