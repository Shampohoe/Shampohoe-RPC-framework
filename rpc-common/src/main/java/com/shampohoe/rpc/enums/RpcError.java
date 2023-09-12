package com.shampohoe.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName:RpcError
 * Package:com.shampohoe.rpc.enums
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/13 0:20
 * #Version 1.1
 */
@AllArgsConstructor
@Getter
public enum RpcError {

    SERVICE_INVOCATION_FAILURE("服务调用出现失败"),
    SERVICE_NOT_FOUND("找不到对应的服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现接口");

    private final String message;
}
