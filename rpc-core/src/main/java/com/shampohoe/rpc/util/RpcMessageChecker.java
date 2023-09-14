package com.shampohoe.rpc.util;

import com.shampohoe.rpc.entity.RpcRequest;
import com.shampohoe.rpc.entity.RpcResponse;
import com.shampohoe.rpc.enums.ResponseCode;
import com.shampohoe.rpc.enums.RpcError;
import com.shampohoe.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:RpcMessageChecker
 * Package:com.shampohoe.rpc.util
 * Description: 检查响应和请求
 *
 * @Author kkli
 * @Create 2023/9/15 1:31
 * #Version 1.1
 */
@Slf4j
public class RpcMessageChecker {
    private static final String INTERFACE_NAME = "interfaceName";

    private RpcMessageChecker(){
    }

    public static void check(RpcRequest rpcRequest, RpcResponse rpcResponse){
        if(rpcResponse == null) {
            log.error("调用服务失败，serviceName:{}", rpcRequest.getInterfaceName());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
        //响应与请求的请求号不同
        if(!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcError.RESPONSE_NOT_MATCH, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
        //调用失败
        if(rpcResponse.getStatusCode() == null || !rpcResponse.getStatusCode().equals(ResponseCode.SUCCESS.getCode())){
            log.error("调用服务失败，serviceName:{}，RpcResponse:{}", rpcRequest.getInterfaceName(), rpcResponse);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
