package com.geebar.geerpc.registry;

import cn.hutool.core.util.StrUtil;
import com.geebar.geerpc.spi.SpiLoader;

/**
 * 注册中心工厂
 */
public class RegistryFactory {
    private static final String DEFAULT_KEY = "etcd";

    public static Registry getInstance(String key) {
        SpiLoader loader = SpiLoader.getInstance();

        String finalKey = StrUtil.isBlank(key) ? DEFAULT_KEY : key;

        return loader.getService(Registry.class, finalKey);
    }
}
