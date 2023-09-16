package com.shampohoe.rpc.provider;

import com.shampohoe.rpc.enums.RpcError;
import com.shampohoe.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName:ServiceProviderImpl
 * Package:com.shampohoe.rpc.provider
 * Description:  默认的服务注册表,保存服务端本地服务
 *
 * @Author kkli
 * @Create 2023/9/16 3:48
 * #Version 1.1
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void addServiceProvider(T service, Class<T> serviceClass) {
        String serviceName = serviceClass.getCanonicalName();
        if(registeredService.contains(serviceName)) return;
        registeredService.add(serviceName);
        // com.whc.test.UserService -> com.whc.test.UserServiceImpl
        serviceMap.put(serviceName, service);
        log.info("向接口: {} 注册服务: {}", service.getClass().getInterfaces(), serviceName);
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        // com.whc.test.UserService -> com.whc.test.UserServiceImpl
        Object service = serviceMap.get(serviceName);
        if(service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
