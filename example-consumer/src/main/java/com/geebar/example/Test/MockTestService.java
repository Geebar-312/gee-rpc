package com.geebar.example.Test;

import com.geebar.geerpc.config.RpcConfig;
import com.geebar.geerpc.model.RpcRequest;
import com.geebar.geerpc.model.RpcResponse;
import com.geebar.geerpc.server.HttpServer;

public interface MockTestService {
    // 测试基本类型
    int getBasicInt();

    // 测试 RpcConfig 对象
    RpcConfig getRpcConfig();

    // 测试 RpcRequest 对象
    RpcRequest getRpcRequest();

    // 测试 RpcResponse 对象
    RpcResponse getRpcResponse();

    // 测试 HttpServer (接口类型)
    HttpServer getHttpServer();

    // 测试未处理的类型 (目前会返回 null)
    String getString();
}