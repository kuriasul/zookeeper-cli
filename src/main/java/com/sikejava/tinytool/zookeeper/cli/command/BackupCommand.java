package com.sikejava.tinytool.zookeeper.cli.command;

import com.sikejava.tinytool.zookeeper.cli.service.BackupService;
import com.sikejava.tinytool.zookeeper.cli.component.zookeeper.ZookeeperTemplate;

import org.apache.commons.lang3.StringUtils;

import io.airlift.airline.Command;
import io.airlift.airline.Option;

/**
 * 备份命令
 *
 * @author skjv2014@163.com
 * @date 2022-06-27 10:20:24
 */
@Command(name = "backup", description = "备份数据")
public class BackupCommand extends AbstractCommand {

    @Option(name = {"-f", "--file"}, title = "文件路径", description = "数据备份文件的路径", required = true)
    public String filePath;

    @Option(name = {"-p", "--exclude-pattern"}, title = "正则排除", description = "节点路径匹配到此正则的节点不进行备份")
    public String excludePattern;

    public String getFilePath() {
        return filePath;
    }

    public String getExcludePattern() {
        return excludePattern;
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

        // 备份数据
        BackupService backupService = new BackupService();
        backupService.backupData2File(zookeeperTemplate, this);
    }
}
