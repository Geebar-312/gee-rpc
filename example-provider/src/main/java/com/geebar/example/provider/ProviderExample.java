package com.geebar.example.provider;

import com.geebar.example.common.service.UserService;
import com.geebar.geerpc.RpcApplication;
import com.geebar.geerpc.config.RpcConfig;
import com.geebar.geerpc.registry.LocalRegistry;
import com.geebar.geerpc.server.HttpServer;
import com.geebar.geerpc.server.VertxHttpServer;

import java.io.FileNotFoundException;


/**
 * 服务提供者示例
 */
public class ProviderExample {

    public static void main(String[] args) {

        RpcApplication.init(".yml");
        System.out.println("Consumer config: " + RpcApplication.getRpcConfig());
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
