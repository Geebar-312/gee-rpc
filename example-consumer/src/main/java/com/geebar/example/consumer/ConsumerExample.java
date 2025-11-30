package com.geebar.example.consumer;

import com.geebar.example.common.model.User;
import com.geebar.example.common.service.UserService;
import com.geebar.geerpc.RpcApplication;
import com.geebar.geerpc.config.RpcConfig;
import com.geebar.geerpc.proxy.ServiceProxyFactory;
import com.geebar.geerpc.utils.ConfigUtils;

public class ConsumerExample {
    public static void main(String[] args) {
        RpcConfig rpc=ConfigUtils.loadConfig(RpcConfig.class,"rpc");
        System.out.println(rpc.getName());
        System.out.println(rpc.getVersion());
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("geebar");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
