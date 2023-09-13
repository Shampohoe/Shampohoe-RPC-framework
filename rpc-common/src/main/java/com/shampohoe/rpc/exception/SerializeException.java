package com.shampohoe.rpc.exception;

/**
 * ClassName:SerializeException
 * Package:com.shampohoe.rpc.exception
 * Description:序列化异常
 *
 * @Author kkli
 * @Create 2023/9/14 0:59
 * #Version 1.1
 */
public class SerializeException extends RuntimeException{
    public SerializeException(String msg){
        super(msg);
    }
}
