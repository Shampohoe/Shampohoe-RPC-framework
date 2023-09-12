package com.shampohoe.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName:ResponseCode
 * Package:com.shampohoe.rpc.enums
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/12 21:02
 * #Version 1.1
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {
    SUCCESS(200,"调用方法成功"),
    FAIL(500,"调用方法失败"),
    NOT_FOUND_METHOD(501,"未找到指定方法"),
    NOT_FOUND_CLASS(502,"未找到指定类");

    private final int code;
    private final String message;
}
