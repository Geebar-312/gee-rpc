package com.geebar.geerpc.spi;


import cn.hutool.core.io.resource.ResourceUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI 加载器(支持键值对映射）
 */
@Slf4j
public class SpiLoader {

    // 存储：接口全限定名 -> (Key -> 实现类)
    private final Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();

    // 缓存：实现类全限定名 -> 实例对象
    private final Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    // SPI 配置文件目录
    private static final String SPI_RPC_SYSTEM_DIR = "META-INF/rpc/system/";
    private static final String SPI_RPC_CUSTOM_DIR = "META-INF/rpc/custom/";
    private static final String[] SPI_LOAD_DIR = new String[]{SPI_RPC_SYSTEM_DIR, SPI_RPC_CUSTOM_DIR};

    // 单例模式
    private static volatile SpiLoader instance;

    private SpiLoader() {}

    public static SpiLoader getInstance() {
        if (instance == null) {
            synchronized (SpiLoader.class) {
                if (instance == null) {
                    instance = new SpiLoader();
                }
            }
        }
        return instance;
    }

    public <T> T getService(Class<T> tClass, String key) {
        String tClassName = tClass.getName();
        // 1. 懒加载：如果没加载过该接口，先扫描文件
        if (!loaderMap.containsKey(tClassName)) {
            synchronized (loaderMap) { // 防止并发重复加载
                if (!loaderMap.containsKey(tClassName)) {
                    load(tClass);
                }
            }
        }

        // 2. 获取实现类 Map
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);
        if (keyClassMap == null || !keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SPI 无法找到 %s 的实现, Key=%s", tClassName, key));
        }

        Class<?> implClass = keyClassMap.get(key);
        String implClassName = implClass.getName();

        // 3. 实例化并缓存 (使用 computeIfAbsent 简化单例逻辑)
        Object obj = instanceCache.computeIfAbsent(implClassName, k -> {
            try {
                return implClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("实例化 SPI 失败: " + implClassName, e);
            }
        });

        return tClass.cast(obj);
    }

    private void load(Class<?> loadClass) {
        log.info("加载 SPI 配置文件: {}", loadClass.getName());
        Map<String, Class<?>> keyClassMap = new HashMap<>();

        for (String scanDir : SPI_LOAD_DIR) {
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());
            for (URL resource : resources) {
                try (InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                     BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] strArr = line.split("=");
                        if (strArr.length > 1) {
                            String key = strArr[0].trim();
                            String className = strArr[1].trim();
                            keyClassMap.put(key, Class.forName(className));
                        }
                    }
                } catch (Exception e) {
                    log.error("SPI load error for {}", loadClass.getName(), e);
                }
            }
        }
        loaderMap.put(loadClass.getName(), keyClassMap);
    }
}






