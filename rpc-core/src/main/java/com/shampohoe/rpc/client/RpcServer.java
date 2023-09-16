package com.shampohoe.rpc.client;

import com.shampohoe.rpc.serializer.CommonSerializer;

/**
 * ClassName:RpacServer
 * Package:rpc.client
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/13 12:30
 * #Version 1.1
 */
public interface RpcServer {
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;
    void start();
    <T> void publishService(T service, Class<T> serviceClass);
}
