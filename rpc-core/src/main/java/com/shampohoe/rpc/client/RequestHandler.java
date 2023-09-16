package com.shampohoe.rpc.client;

import com.shampohoe.rpc.entity.RpcRequest;
import com.shampohoe.rpc.entity.RpcResponse;
import com.shampohoe.rpc.enums.ResponseCode;
import com.shampohoe.rpc.provider.ServiceProvider;
import com.shampohoe.rpc.provider.ServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * ClassName:WorkerThread
 * Package:rpc.server
 * Description:  实际执行方法调用的处理器
 *
 * @Author kkli
 * @Create 2023/9/12 22:09
 * #Version 1.1
 */
@Slf4j
public class RequestHandler {
    private static final ServiceProvider serviceProvider;
    static {
        serviceProvider = new ServiceProviderImpl();
    }
    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest,Object service) {
        Object result;
        try{
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e ) {
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND, rpcRequest.getRequestId());
        }
        return result;
    }
}
