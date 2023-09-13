package com.shampohoe.rpc.test;

import com.shampohoe.rpc.api.HelloService;
import rpc.registry.DefaultServiceRegistry;
import rpc.registry.ServiceRegistry;
import rpc.socket.server.SocketServer;

/**
 * ClassName:SocketTestServer
 * Package:com.shampohoe.rpc.test
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/13 12:42
 * #Version 1.1
 */
public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.start(9000);
    }
}
