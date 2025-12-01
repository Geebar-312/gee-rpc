package com.geebar.example.consumer;

import com.geebar.example.common.model.User;
import com.geebar.example.common.service.UserService;
import com.geebar.geerpc.RpcApplication;
import com.geebar.geerpc.proxy.ServiceProxyFactory;

public class ConsumerExample {
    public static void main(String[] args) {


        RpcApplication.init(".yml");
        System.out.println("Consumer config: " + RpcApplication.getRpcConfig());

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("geebar");

        User newUser = userService.getUser(user);
        System.out.println(newUser != null ? newUser.getName() : "user == null");
    }
}

