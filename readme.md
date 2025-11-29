# 手写RPC框架

## 阶段1 极简RPC框架

### 阶段结果

1. 搭建项目和模块

    1. `example-common`:示例代码的公共依赖，包括接口、Model等

    1. `example-consumer`:示例服务消费者代码

    1. `example-provider`:示例服务提供者代码

    1. `rpc-easy`:RPC框架-简易版



#### example-common 模块

| 功能     | 核心组件           |
| -------- | ------------------ |
| 实体类   | User类,设置name    |
| 服务接口 | UserService        |
| 服务方法 | getUser() 返回User |

#### example-consumer 模块

| 功能         | 核心组件                                                     |
| ------------ | ------------------------------------------------------------ |
| 请求客户端   | EasyProviderExample                                          |
| 服务调用     | 获取UserService远程代理对象                                  |
| 请求发送方式 | 基于 **JDK 动态代理**，返回代理对象通过 `HTTPRequest` (Hutool 工具包) 发送请求 |
| 输入参数     | User对象(name字段)                                           |
| 输出结果     | User对象                                                     |

#### example-provider 模块

| 功能         | 核心组件                     |
| ------------ | ---------------------------- |
| 启动类       | EasyProviderExample类        |
| 服务实现     | UserServiceImpl类            |
| 服务方法     | getUser()方法实现            |
| 业务逻辑     | 打印用户名，返回原对象       |

#### gee-rpc-easy 模块

| 功能         | 核心组件                     |
| ------------ | ---------------------------- |
| HTTP服务接口 | HttpServer接口               |
| HTTP服务实现 | VertxHttpServer类            |
| 服务框架     | 基于Vert.x实现               |
| 通信协议     | HTTP协议                     |
