package com.sikejava.tinytool.zookeeper.cli.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class ZooKeeperEnvironmentTransformer implements ClassFileTransformer {
    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperEnvironmentTransformer.class);

    private final String targetClassName;

    private final ClassLoader targetClassLoader;

    private static final String LOG_ENV_METHOD ="logEnv";

    public ZooKeeperEnvironmentTransformer(String className, ClassLoader classLoader) {
        this.targetClassName = className;
        this.targetClassLoader = classLoader;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;

        String finalTargetClassName = this.targetClassName.replaceAll("\\.", "/");
        if (!className.equals(finalTargetClassName)) {
            return byteCode;
        }

        if (loader.equals(targetClassLoader)) {
            logger.info("[Agent] Transforming zookeeper class Environment");
            try {
                ClassPool classPool = ClassPool.getDefault();
                CtClass ctClass = classPool.get(targetClassName);
                CtMethod ctMethod = ctClass.getDeclaredMethod(LOG_ENV_METHOD);

                ctMethod.setBody("return;");

                byteCode = ctClass.toBytecode();
                ctClass.detach();
            } catch (Exception e) {
                logger.error("zookeeper class Environment transform failed", e);
            }
        }
        return byteCode;
    }
}