package com.shampohoe.rpc.provider;

/**
 * ClassName:ServiceProvider
 * Package:com.shampohoe.rpc.provider
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/16 3:48
 * #Version 1.1
 */
public interface ServiceProvider {
    <T> void addServiceProvider(T service, Class<T> serviceClass);

    Object getServiceProvider(String serviceName);
}

