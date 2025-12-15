package com.geebar.geerpc.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geebar.geerpc.model.RpcRequest;
import com.geebar.geerpc.model.RpcResponse;

import java.io.IOException;

/**
 * Json 序列化器
 * 需要处理泛型擦除问题
 */
public class JsonSerializer implements Serializer {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> classType) throws IOException {
        T obj = OBJECT_MAPPER.readValue(bytes, classType);
        if (obj instanceof RpcRequest) {
            return handleRequest((RpcRequest) obj, classType);
        }
        if (obj instanceof RpcResponse) {
            return handleResponse((RpcResponse) obj, classType);
        }
        return obj;
    }

    /**
     *
     * @param rpcRequest rpc 请求
     * @param type       类型
     * @return {@link T}
     * @throws IOException IO异常
     */
    private <T> T handleRequest(RpcRequest rpcRequest, Class<T> type) throws IOException {
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] args = rpcRequest.getArgs();

        // 循环处理每个参数的类型
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> clazz = parameterTypes[i];
            // 检查：如果现在的 args[i] (比如 Map) 不是 clazz (比如 User) 的实例
            if (!clazz.isAssignableFrom(args[i].getClass())) {
                // 1. 把 args[i] (Map) 再次转回 JSON 字节
                byte[] argBytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                // 2. 把 JSON 字节根据正确的类型 (clazz) 再次反序列化
                args[i] = OBJECT_MAPPER.readValue(argBytes, clazz);
            }
        }

        return type.cast(rpcRequest);
    }

    /**
     * @param rpcResponse rpc 响应
     * @param type        类型
     * @return {@link T}
     * @throws IOException IO异常
     */
    private <T> T handleResponse(RpcResponse rpcResponse, Class<T> type) throws IOException {
        // 处理响应数据
        byte[] dataBytes = OBJECT_MAPPER.writeValueAsBytes(rpcResponse.getData());
        rpcResponse.setData(OBJECT_MAPPER.readValue(dataBytes, rpcResponse.getDataType()));
        return type.cast(rpcResponse);
    }
}
