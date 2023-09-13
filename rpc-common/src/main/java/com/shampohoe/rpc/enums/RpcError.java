package com.shampohoe.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName:RpcError
 * Package:com.shampohoe.rpc.enums
 * Description: Rpc调用过程中出现的错误
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
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现接口"),
    UNKNOWN_PROTOCOL("不识别的协议包"),
    UNKNOWN_SERIALIZER("不识别的(反)序列化器"),
    UNKNOWN_PACKAGE_TYPE("不识别的数据包类型");

    private final String message;
}
