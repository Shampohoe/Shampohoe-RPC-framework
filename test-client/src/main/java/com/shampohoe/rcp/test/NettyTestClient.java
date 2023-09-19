package com.shampohoe.rcp.test;

import com.shampohoe.rpc.api.HelloObject;
import com.shampohoe.rpc.api.HelloService;
import com.shampohoe.rpc.client.RpcClient;
import com.shampohoe.rpc.client.RpcClientProxy;
import com.shampohoe.rpc.loadbalance.loadbalancer.RoundLoadBalance;
import com.shampohoe.rpc.netty.client.NettyClient;
import com.shampohoe.rpc.serializer.CommonSerializer;
import com.shampohoe.rpc.serializer.KryoSerializer;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:NettyTestClient
 * Package:com.shampohoe.rcp.test
 * Description:   测试用Netty客户端
 *
 * @Author kkli
 * @Create 2023/9/13 15:07
 * #Version 1.1
 */
@Slf4j
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.KRYO_SERIALIZER,new RoundLoadBalance());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "this is netty style");
        String res = helloService.hello(object);
        System.out.println(res);

        log.info("aaaaa");
        String res2 = helloService.hello(object);
        System.out.println(res2);
        log.info("bbbbbbb");
    }
}
