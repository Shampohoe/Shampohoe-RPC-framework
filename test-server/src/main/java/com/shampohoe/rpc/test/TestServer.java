package com.shampohoe.rpc.test;

import com.shampohoe.rpc.api.HelloService;
import rpc.server.RpcServer;

/**
 * ClassName:TestClient
 * Package:com.shampohoe.rpc.test
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/12 22:35
 * #Version 1.1
 */
public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        //注册HelloServiceImpl服务
        rpcServer.register(helloService, 9000);
    }
}
