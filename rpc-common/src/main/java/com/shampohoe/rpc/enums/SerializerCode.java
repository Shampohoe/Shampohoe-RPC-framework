package com.shampohoe.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName:SerializerCode
 * Package:com.shampohoe.rpc.enums
 * Description:  字节流中标识序列化和反序列化器
 *
 * @Author kkli
 * @Create 2023/9/13 14:46
 * #Version 1.1
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {
    KRYO(0),
    JSON(1);

    private final int code;
}