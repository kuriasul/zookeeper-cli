package com.sikejava.tinytool.zookeeper.cli;

import com.sikejava.tinytool.zookeeper.cli.command.SetDataCommand;
import com.sikejava.tinytool.zookeeper.cli.component.log.DumpLogger;
import com.sikejava.tinytool.zookeeper.cli.command.BackupCommand;
import com.sikejava.tinytool.zookeeper.cli.command.HelpCommand;
import com.sikejava.tinytool.zookeeper.cli.command.RestoreCommand;

import io.airlift.airline.Cli;
import io.airlift.airline.ParseArgumentsUnexpectedException;
import io.airlift.airline.ParseOptionMissingException;
import io.airlift.airline.ParseOptionMissingValueException;

/**
 * zookeeper dump 应用
 *
 * @author skjv2014@163.com
 * @date 2022-06-26 14:00:17
 */
public class Application {

    Application() {
    }
 
    public static void main(String[] args) {
        try {
            Cli<Runnable> cli = Cli.<Runnable>builder("zk")
                    .withDefaultCommand(HelpCommand.class)
                    .withCommands(HelpCommand.class, BackupCommand.class, RestoreCommand.class, SetDataCommand.class)
                    .build();

            cli.parse(args).run();
        } catch (ParseArgumentsUnexpectedException | ParseOptionMissingException | ParseOptionMissingValueException e) {
            DumpLogger.error(e.getMessage(), e);
            System.out.println("error: " + e.getMessage());
        } catch (Exception e) {
            String errorMsg = "系统错误, 请联系开发人员";

            DumpLogger.error(errorMsg, e);
            System.out.println("error: " + errorMsg);
        }
    }
}