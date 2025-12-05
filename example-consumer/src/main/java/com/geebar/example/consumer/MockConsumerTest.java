package com.geebar.example.consumer;

import com.geebar.example.Test.MockTestService;
import com.geebar.geerpc.config.RpcConfig;
import com.geebar.geerpc.model.RpcRequest;

import com.geebar.geerpc.proxy.MockServiceProxy;

import java.lang.reflect.Proxy;

public class MockConsumerTest {

    public static void main(String[] args) {

        MockTestService service = (MockTestService) Proxy.newProxyInstance(
                MockConsumerTest.class.getClassLoader(),
                new Class[]{MockTestService.class},
                new MockServiceProxy()
        );

        System.out.println("========== 测试开始 ==========");

        System.out.println("int mock: " + service.getBasicInt());

        System.out.println("\nRpcConfig mock:");
        RpcConfig config = service.getRpcConfig();
        if (config != null) {
            System.out.println("   Name: " + config.getName());
            System.out.println("   Host: " + config.getServerHost());
            System.out.println("   Port: " + config.getServerPort());
            System.out.println("   Version: " + config.getVersion());
        }

        System.out.println("\nRpcRequest mock:");
        RpcRequest request = service.getRpcRequest();
        if (request != null) {
            System.out.println("   Method: " + request.getMethodName());
            System.out.println("   Service: " + request.getServiceName());
        }


        System.out.println("\nString mock (未在Proxy中定义):");
        System.out.println("   Value: " + service.getString()); // 预期为 null

        System.out.println("========== 测试结束 ==========");
    }
}