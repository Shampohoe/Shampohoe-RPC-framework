package com.shampohoe.rpc.client;

import com.shampohoe.rpc.entity.RpcRequest;
import com.shampohoe.rpc.serializer.CommonSerializer;

/**
 * ClassName:RpcClient
 * Package:rpc.client
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/13 12:31
 * #Version 1.1
 */
public interface RpcClient {
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;
    Object sendRequest(RpcRequest rpcRequest);

}
