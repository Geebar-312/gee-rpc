package com.geebar.example.provider;



import com.geebar.example.common.service.UserService;
import com.geebar.geerpc.RpcApplication;
import com.geebar.geerpc.registry.LocalRegistry;
import com.geebar.geerpc.server.HttpServer;
import com.geebar.geerpc.server.VertxHttpServer;


/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        //注册服务
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);
        //启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}