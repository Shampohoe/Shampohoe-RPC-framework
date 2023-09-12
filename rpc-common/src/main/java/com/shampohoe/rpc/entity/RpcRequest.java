package com.shampohoe.rpc.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * ClassName:RpcRequest
 * Package:com.shampohoe.rpc.entity
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/12 20:53
 * #Version 1.1
 */
@Data
@Builder
public class RpcRequest implements Serializable {
    /**
     * 待调用接口名称
     */
    private String interfaceName;
    /**
     * 待调用方法名称
     */
    private String methodName;
    /**
     * 待调用方法的参数
     */
    private Object[] parameters;
    /**
     * 待调用方法的参数类型
     */
    private Class<?>[] paramTypes;
}
