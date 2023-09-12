package com.shampohoe.rpc.test;

import com.shampohoe.rpc.api.HelloService;
import rpc.registry.DefaultServiceRegistry;
import rpc.registry.ServiceRegistry;
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
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(9000);
    }
}
