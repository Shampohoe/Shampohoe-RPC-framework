package com.shampohoe;

import com.shampohoe.rpc.api.HelloObject;
import com.shampohoe.rpc.api.HelloService;
import rpc.client.RpcClientProxy;

/**
 * ClassName:TestClient
 * Package:com.shampohoe
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/12 22:36
 * #Version 1.1
 */
public class TestClient {
    public static void main(String[] args) {
        //接口与代理对象之间的中介对象
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        //创建代理对象
        HelloService helloService = proxy.getProxy(HelloService.class);
        //接口方法的参数对象
        HelloObject object = new HelloObject(12, "This is test message");
        //由动态代理可知，代理对象调用hello()实际会执行invoke()
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
