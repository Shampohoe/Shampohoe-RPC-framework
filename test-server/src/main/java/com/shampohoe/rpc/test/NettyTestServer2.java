package com.shampohoe.rpc.test;

import com.shampohoe.rpc.api.HelloService;
import com.shampohoe.rpc.netty.server.NettyServer;
import com.shampohoe.rpc.serializer.CommonSerializer;

/**
 * ClassName:NettyTestServer2
 * Package:com.shampohoe.rpc.test
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/17 17:00
 * #Version 1.1
 */
public class NettyTestServer2 {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        // 服务端需要把自己的ip，端口给注册中心
        NettyServer server = new NettyServer("127.0.0.1", 9002, CommonSerializer.KRYO_SERIALIZER);
        server.publishService(helloService, HelloService.class);
        server.start();
    }
}
