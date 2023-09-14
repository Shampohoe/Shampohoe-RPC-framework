package com.shampohoe.rpc.test;

import com.shampohoe.rpc.api.HelloService;
import com.shampohoe.rpc.netty.server.NettyServer;
import com.shampohoe.rpc.registry.DefaultServiceRegistry;
import com.shampohoe.rpc.registry.ServiceRegistry;
import com.shampohoe.rpc.serializer.KryoSerializer;

/**
 * ClassName:NettyTestServer
 * Package:com.shampohoe.rpc.test
 * Description:测试用Netty服务端
 *
 * @Author kkli
 * @Create 2023/9/13 15:07
 * #Version 1.1
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.setSerializer(new KryoSerializer());
        server.start(9999);
    }
}
