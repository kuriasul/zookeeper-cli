package com.sikejava.tinytool.zookeeper.cli.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

/**
 * 插桩代理
 *
 * @author skjv2014@163.com
 * @date 2022-06-28 11:35:02
 */
public class InstrumentationAgent {
    private static final Logger logger = LoggerFactory.getLogger(InstrumentationAgent.class);

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        logger.info("[Agent] In premain method");
        transformClass("org.apache.zookeeper.Environment", instrumentation);
    }

    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        logger.info("[Agent] In agentmain method");
        transformClass("org.apache.zookeeper.Environment", instrumentation);
    }

    private static void transformClass(String className, Instrumentation instrumentation) {
        Class<?> targetCls = null;
        ClassLoader targetClassLoader;
        try {
            targetCls = Class.forName(className);
            targetClassLoader = targetCls.getClassLoader();
            transform(targetCls, targetClassLoader, instrumentation);
            return;
        } catch (Exception e) {
            logger.error("Class [{}] not found with Class.forName", className, e);
        }
        for(Class<?> clazz: instrumentation.getAllLoadedClasses()) {
            if(clazz.getName().equals(className)) {
                targetCls = clazz;
                targetClassLoader = targetCls.getClassLoader();
                transform(targetCls, targetClassLoader, instrumentation);
                return;
            }
        }
        throw new RuntimeException("Failed to find class [" + className + "]");
    }

    private static void transform(Class<?> clazz, ClassLoader classLoader, Instrumentation instrumentation) {
        ZooKeeperEnvironmentTransformer dt = new ZooKeeperEnvironmentTransformer(clazz.getName(), classLoader);
        instrumentation.addTransformer(dt, true);
        try {
            instrumentation.retransformClasses(clazz);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Transform failed for: [" + clazz.getName() + "]", ex);
        }
    }
}
