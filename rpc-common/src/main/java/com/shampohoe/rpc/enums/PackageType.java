package com.shampohoe.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName:PackageType
 * Package:com.shampohoe.rpc.enums
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/13 14:47
 * #Version 1.1
 */
@Getter
@AllArgsConstructor
public enum PackageType {

    REQUEST_PACK(0),
    RESPONSE_PACK(1),
    HEARTBEAT_PACK(2);

    private final int code;
}
