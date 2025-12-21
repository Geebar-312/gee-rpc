package com.geebar.geerpc.registry;

import com.geebar.geerpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryServiceCache {

    /**
     * 缓存服务列表
     */
    Map<String, List<ServiceMetaInfo>> serviceCache = new ConcurrentHashMap<>();

    /**
     * 写入缓存
     * @param serviceKey
     * @param newServiceCache
     */
    public void writeCache(String serviceKey, List<ServiceMetaInfo> newServiceCache) {
        this.serviceCache.put(serviceKey, newServiceCache);
    }


    /**
     * 读取缓存
     * @param serviceKey
     * @return
     */
    public List<ServiceMetaInfo> readCache(String serviceKey) {
        return this.serviceCache.get(serviceKey);
    }

    /**
     * 清空缓存
     */
    public void clearCache(String serviceKey){
        this.serviceCache.remove(serviceKey);
    }
}
