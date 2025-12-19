package com.geebar.geerpc;

import cn.hutool.core.util.StrUtil;
import com.geebar.geerpc.config.RegistryConfig;
import com.geebar.geerpc.config.RpcConfig;
import com.geebar.geerpc.constant.RpcConstant;
import com.geebar.geerpc.registry.Registry;
import com.geebar.geerpc.registry.RegistryFactory;
import com.geebar.geerpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * RPC 框架应用启动类
 */
@Slf4j
public class RpcApplication {

    // Rpc配置单例
    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig newConfig) {
        rpcConfig = newConfig;
        log.info("Rpc Config init succeed!, config = {}", newConfig);
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init, config = {}", registryConfig);
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }


    public static void init() {
        init(RpcConstant.PROP_CONFIG_SUFFIX);
    }


    public static void initByEnv(String environment) {
        init(RpcConstant.PROP_CONFIG_SUFFIX, environment);
    }


    public static void init(String suffix) {
        init(suffix, null);
    }


    public static void init(String suffix, String environment) {
        RpcConfig config = loadConfig(suffix, environment);
        init(config);
    }


    private static RpcConfig loadConfig(String suffix, String environment) {
        if (StrUtil.isBlank(suffix)) {
            suffix = RpcConstant.PROP_CONFIG_SUFFIX;
        }

        // 1. 处理 .properties
        if (RpcConstant.PROP_CONFIG_SUFFIX.equals(suffix)) {
            return ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX, environment);
        }

        // 2. 处理 .yml / .yaml
        String[] ymlSuffixes = RpcConstant.YAML_CONFIG_SUFFIX.split(";");
        for (String yml : ymlSuffixes) {
            if (suffix.equals(yml)) {
                return ConfigUtils.loadConfigYaml(RpcConfig.class, yml, environment);
            }
        }

        throw new RuntimeException("Unsupported RpcConfig file suffix: " + suffix);
    }

    /**
     * 获取配置（双重检查懒加载）
     * 如果没有初始化过，则执行默认初始化
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init(); // 默认加载 application.properties
                }
            }
        }
        return rpcConfig;
    }
}