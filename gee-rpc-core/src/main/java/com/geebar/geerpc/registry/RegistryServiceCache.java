package com.geebar.geerpc.registry;

import com.geebar.geerpc.model.ServiceMetaInfo;

import java.util.List;

public class RegistryServiceCache {

    /**
     * 缓存服务列表
     */
    List<ServiceMetaInfo> serviceCache;

    /**
     * 写入缓存
     * @param newServiceCache
     */
    public void writeCache(List<ServiceMetaInfo> newServiceCache){
        this.serviceCache = newServiceCache;
    }

    /**
     * 读取缓存
     * @return
     */
    public List<ServiceMetaInfo> readCache(){
        return this.serviceCache;
    }

    /**
     * 清空缓存
     */
    public void clearCache(){
        this.serviceCache = null;
    }
}
