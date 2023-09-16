package com.shampohoe.rcp.test;

import com.shampohoe.rpc.api.HelloObject;
import com.shampohoe.rpc.api.HelloService;
import com.shampohoe.rpc.client.RpcClient;
import com.shampohoe.rpc.client.RpcClientProxy;
import com.shampohoe.rpc.netty.client.NettyClient;
import com.shampohoe.rpc.serializer.CommonSerializer;
import com.shampohoe.rpc.serializer.KryoSerializer;

/**
 * ClassName:NettyTestClient
 * Package:com.shampohoe.rcp.test
 * Description:   测试用Netty客户端
 *
 * @Author kkli
 * @Create 2023/9/13 15:07
 * #Version 1.1
 */
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.KRYO_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "this is netty style");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
