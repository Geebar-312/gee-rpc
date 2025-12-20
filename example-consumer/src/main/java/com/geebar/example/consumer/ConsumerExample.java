package com.geebar.example.consumer;

import com.geebar.example.common.model.User;
import com.geebar.example.common.service.UserService;
import com.geebar.geerpc.RpcApplication;
import com.geebar.geerpc.proxy.ServiceProxyFactory;

public class ConsumerExample {
    public static void main(String[] args) throws InterruptedException {


        RpcApplication.init(".yml");
        System.out.println("Consumer config: " + RpcApplication.getRpcConfig());

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("geebar");

//        User newUser = userService.getUser(user);
//
//        if (newUser != null) {
//            System.out.println(newUser.getName());
//        } else {
//            System.out.println("user == null");
//        }
//        System.out.println("--- 准备执行第二次调用 ---");
//        long number = userService.getNumber();
//        System.out.println("第二次调用结果：" + number);
        // 持续运行，每隔 10 秒发起一次调用
        while (true) {
            System.out.println("---------------------------------------");
            System.out.println(">>> 准备发起 RPC 调用...");
            try {
                User newUser = userService.getUser(user);
                System.out.println(">>> 调用结果: " + (newUser != null ? newUser.getName() : "null"));
            } catch (Exception e) {
                System.out.println(">>> 调用失败: " + e.getMessage());
            }

            System.out.println(">>> 请在此期间手动停止 Provider 或删除 Etcd 中的 Key...");
            Thread.sleep(10000); // 每 10 秒调用一次，给你留出操作时间
        }
    }
}



