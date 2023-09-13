package rpc.client;

import com.shampohoe.rpc.entity.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import rpc.socket.client.SocketClient;

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
    private final RpcClient client;

    public RpcClientProxy(RpcClient client) {
        this.client = client;
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
        RpcRequest rpcRequest = new RpcRequest(method.getDeclaringClass().getName(),
                method.getName(), args, method.getParameterTypes());
        return client.sendRequest(rpcRequest);
    }
}
