package com.geebar.geerpc.config;

import lombok.Data;

/**
 * RPC 配置
 */
@Data
public class RpcConfig {

    /**
     * 服务名称
     */
    private String name="geerpc-base";

    /**
     * 服务版本
     */
    private String version="1.0";

    /**
     * 服务主机
     */
    private String serverHost="localhost";

    /**
     * 服务端口
     */
    private Integer serverPort=8080;

    /**
     * 模拟调用
     */
    private boolean mock=false;

}
