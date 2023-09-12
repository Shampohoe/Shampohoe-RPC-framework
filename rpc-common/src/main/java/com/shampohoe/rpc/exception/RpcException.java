package com.shampohoe.rpc.exception;

import com.shampohoe.rpc.enums.RpcError;

/**
 * ClassName:RpcException
 * Package:com.shampohoe.rpc.exception
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/13 0:21
 * #Version 1.1
 */
public class RpcException extends RuntimeException{

    public RpcException(RpcError error, String detail){
        super(error.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause){
        super(message, cause);
    }

    public RpcException(RpcError error){
        super(error.getMessage());
    }
}
