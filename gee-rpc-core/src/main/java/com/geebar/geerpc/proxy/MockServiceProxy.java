package com.geebar.geerpc.proxy;

import com.geebar.geerpc.config.RpcConfig;
import com.geebar.geerpc.model.RpcRequest;
import com.geebar.geerpc.model.RpcResponse;
import com.geebar.geerpc.server.HttpServer;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Mock服务代理
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {

    public static final Faker faker= new Faker(new Locale("zh-CN"));

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke{}",method.getReturnType());
        return getDeaultObject(methodReturnType);
    }

    private Object getDeaultObject(Class<?> returnType){
        // 基本类型
        if (returnType.isPrimitive()) {
            if (returnType == boolean.class) {
                return false;
            } else if (returnType == short.class) {
                return (short) 0;
            } else if (returnType == int.class) {
                return 0;
            } else if (returnType == long.class) {
                return 0L;
            } else if (returnType == float.class) {
                return 0.0f;
            } else if (returnType == double.class) {
                return 0.0;
            } else if (returnType == char.class) {
                return '\u0000';
            } else if (returnType == byte.class) {
                return (byte) 0;
            }
        }
        if (returnType == RpcRequest.class || returnType == RpcResponse.class) {
            return getDefaultRpcReqResp(returnType);
        }

        //Rpc - Config
        if (returnType == RpcConfig.class) {
            return getDefaultRpcConfig();
        }

        //Http Server
        if (returnType.isAssignableFrom(HttpServer.class)) {
            return getDefaultHttpServer();
        }

        //其他类型
        return null;
    }

    private HttpServer getDefaultHttpServer() {
        return port -> System.out.println(
                faker.bothify("Default-HttpServer-##; Start on Port:" + port)
        );
    }

    private RpcConfig getDefaultRpcConfig() {
        RpcConfig rpcConfig = new RpcConfig();

        //faker属性值
        rpcConfig.setMock(true);
        rpcConfig.setName(faker.bothify("Default-RpcConfig-##"));
        rpcConfig.setServerHost(faker.internet().ipV4Address());
        rpcConfig.setServerPort(Integer.valueOf(faker.numerify("####")));
        rpcConfig.setVersion(faker.bothify("v0.#"));

        return rpcConfig;
    }

    //返回 Request Response Model 对象
    private Object getDefaultRpcReqResp(Class<?> type) {
        if (type == RpcRequest.class) {
            RpcRequest request = new RpcRequest();

            //Faker 属性值
            request.setServiceName(faker.bothify("DefaultService-##"));
            request.setMethodName(faker.bothify("Default-Method-##"));
            request.setArgs(new Object[]{"Default-Params-##"});
            request.setParameterTypes(new Class<?>[]{faker.getClass()});

            return request;
        } else if (type == RpcResponse.class) {
            RpcResponse rpcResponse = new RpcResponse();

            //faker 属性值
            rpcResponse.setMessage(faker.bothify("Default-Message-??"));
            rpcResponse.setException(new RuntimeException());
            rpcResponse.setData(faker.getClass());
            rpcResponse.setDataType(faker.getClass());

            return rpcResponse;
        }
        return null;
    }
}



