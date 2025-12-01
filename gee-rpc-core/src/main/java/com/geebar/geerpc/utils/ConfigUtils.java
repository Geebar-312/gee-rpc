package com.geebar.geerpc.utils;

import cn.hutool.core.io.resource.NoResourceException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.geebar.geerpc.constant.RpcConstant;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.LoaderOptions; // 1. 确保引入这个类
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 配置工具类
 */

@Slf4j
public class ConfigUtils {

    /**
     * 加载 YAML 格式配置文件
     */
    public static <T> T loadConfigYaml(Class<T> tClass, String suffix, String environment) {
        StringBuilder fileName = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            fileName.append("-").append(environment);
        }
        fileName.append(suffix);

        try (InputStream in = ConfigUtils.class.getClassLoader().getResourceAsStream(fileName.toString())) {
            // 1. 如果文件不存在，直接返回默认对象（使用类中定义的默认值）
            if (in == null) {
                log.info("YAML file not found: {}, utilizing default config.", fileName);
                return tClass.newInstance();
            }

            Yaml yaml = new Yaml();
            Map<String, Object> map = yaml.load(in);

            // 2. 构造 Yaml 实例（带 LoaderOptions 修复兼容性）
            Yaml beanYaml = new Yaml(new Constructor(tClass, new LoaderOptions()));

            // 3. 处理前缀
            if (map != null && map.containsKey(RpcConstant.DEFAULT_CONFIG_PREFIX)) {
                Object configNode = map.get(RpcConstant.DEFAULT_CONFIG_PREFIX);
                String nodeYamlStr = new Yaml().dump(configNode);
                return beanYaml.load(nodeYamlStr);
            }

            String fullYamlStr = new Yaml().dump(map);
            return beanYaml.load(fullYamlStr);

        } catch (Exception e) {
            log.error("Load YAML config failed.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载 Properties 配置对象
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".properties");
        try {
            Props props = new Props(configFileBuilder.toString(), StandardCharsets.UTF_8);
            return props.toBean(tClass, prefix);
        } catch (NoResourceException e) {
            // 捕获到文件不存在异常，打印日志，并返回默认对象
            log.info("Properties file not found: {}, utilizing default config.", configFileBuilder);
            try {
                return tClass.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException("Create default config instance failed", ex);
            }
        }
    }

    public static <T> T loadConfigYaml(Class<T> tClass, String suffix) {
        return loadConfigYaml(tClass, suffix, null);
    }
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, null);
    }
}