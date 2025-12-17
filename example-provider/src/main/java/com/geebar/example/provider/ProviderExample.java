package com.geebar.example.provider;

import com.geebar.example.common.service.UserService;
import com.geebar.geerpc.RpcApplication;
import com.geebar.geerpc.config.RegistryConfig;
import com.geebar.geerpc.config.RpcConfig;
import com.geebar.geerpc.model.ServiceMetaInfo;
import com.geebar.geerpc.registry.Registry;
import com.geebar.geerpc.registry.RegistryFactory;
import com.geebar.geerpc.registry.impl.LocalRegistry;
import com.geebar.geerpc.server.HttpServer;
import com.geebar.geerpc.server.VertxHttpServer;


/**
 * 服务提供者示例
 */
public class ProviderExample {

    public static void main(String[] args) {

        RpcApplication.init(".yml");
        System.out.println("Consumer config: " + RpcApplication.getRpcConfig());
        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        //注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
