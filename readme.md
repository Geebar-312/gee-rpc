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

| 功能           | 核心组件                                                     |
| -------------- | ------------------------------------------------------------ |
| Web服务器      | **Vert.x**                                                   |
| 本地服务注册器 | `LocalRegistry` <br>基于 `ConcurrentHashMap`<br> + `key` 为服务名称<br> + `value` 为服务实现类全类名 |
| 序列化器       | `JdkSerializer`,基于JDK原生序列化                            |
| 请求处理器     | `HttpServerHandler` 借助序列化器反序列化 HTTP 请求，调用本地服务注册/序列化返回响应。 |
| 动态代理处理器 | `ServiceProxyFactory`返回 `ServiceProxy`实例，简化对象代理过程 |

![image-20251130131230990](https://s2.loli.net/2025/11/30/avnyDts48AKgFCz.png)

## 阶段2 全局配置加载

RPC框架使用中会涉及许多配置信息，且RPC框架需要被其他应用引入。应当允许引入框架的项目通过编写配置文件来自定义配置。

### 阶段成果

1. 支持基于 `application.properties` 文件加载全局配置
2. 支持区分 `dev`, `prod` 多环境配置文件
3. `(扩展)` 工具类 SnakeYAML 支持基于 `.yml / .yaml` 格式文件加载全局配置
4. `(扩展)` 配置文件支持中文

## 阶段3 Mock接口开发

Mock接口开发便于使用者与开发者快速调用接口，跑通业务，不必依赖真实的远程服务，提高使用体验

### 阶段成果

1. 支持基于配置开启接口 Mock
2. 基于 JDK + JavaFaker 支持多种数据类型返回默认值
3. 基于 JavaFaker 库完善 Mock 机制

![ 2025-12-05 195049.png](https://s2.loli.net/2025/12/05/9LN6H8FJzATn3kd.png)

