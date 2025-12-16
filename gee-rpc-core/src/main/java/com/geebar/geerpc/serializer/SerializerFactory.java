package com.geebar.geerpc.serializer;


import cn.hutool.core.util.StrUtil;
import com.geebar.geerpc.spi.SpiLoader;

/**
 * 序列化器工厂（用SPI代替HashMap硬编码）
 */
public class SerializerFactory {

    private static final String DEFAULT_KEY = "jdk";

    public static Serializer getInstance(String key) {
        SpiLoader loader = SpiLoader.getInstance();

        String finalKey = StrUtil.isBlank(key) ? DEFAULT_KEY : key;

        return loader.getService(Serializer.class, finalKey);
    }
}
