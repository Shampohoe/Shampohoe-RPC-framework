package com.shampohoe.rpc.test;

import com.shampohoe.rpc.api.HelloService;
import com.shampohoe.rpc.netty.server.NettyServer;
import com.shampohoe.rpc.serializer.CommonSerializer;

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
        // 服务端需要把自己的ip，端口给注册中心
        NettyServer server = new NettyServer("127.0.0.1", 9000, CommonSerializer.KRYO_SERIALIZER);
        server.publishService(helloService, HelloService.class);
        server.start();
    }
}
