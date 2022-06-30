package com.sikejava.tinytool.zookeeper.cli.command;

import com.sikejava.tinytool.zookeeper.cli.component.zookeeper.ZookeeperTemplate;
import com.sikejava.tinytool.zookeeper.cli.service.SetDataService;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import io.airlift.airline.Command;
import io.airlift.airline.Option;

/**
 * 设置数据命令
 *
 * @author skjv2014@163.com
 * @date 2022-06-27 10:20:24
 */
@Command(name = "setdata", description = "设置数据")
public class SetDataCommand extends AbstractCommand {

    @Option(name = {"-p", "--node-pattern"}, title = "节点正则", description = "节点路径匹配到此正则的节点进行设置数据", required = true)
    public String nodePattern;

    @Option(name = {"-d", "--node-data"}, title = "节点数据", description = "节点的数据", required = true)
    public String nodeData;

    public String getNodePattern() {
        return nodePattern;
    }

    public String getNodeData() {
        return nodeData;
    }

    @Override
    public void execute(ZookeeperTemplate zookeeperTemplate) {
        // 处理参数
        if (StringUtils.isBlank(nodePattern)) {
            System.out.println("error: 节点路径正则必须有值");
            return;
        }
        nodeData = Objects.nonNull(nodeData) ? nodeData : "";

        // 恢复数据
        SetDataService setDataService = new SetDataService();
        setDataService.setData(zookeeperTemplate, this);
    }
}
