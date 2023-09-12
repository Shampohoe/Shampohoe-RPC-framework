package rpc.client;

import com.shampohoe.rpc.entity.RpcRequest;
import com.shampohoe.rpc.entity.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ClassName:RpcClientProxy
 * Package:rpc.client
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/12 21:28
 * #Version 1.1
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {
    private String host;
    private int port;

    public RpcClientProxy(String host, int port){
        this.host = host;
        this.port = port;
    }

    //抑制编译器产生警告信息
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        //创建代理对象
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("调用方法：{}#{}", method.getDeclaringClass().getName(), method.getName());
        //客户端向服务端传输的对象,
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        //进行远程调用的客户端
        RpcClient rpcClient = new RpcClient();
        return rpcClient.sendRequest(rpcRequest, host, port);
    }
}
