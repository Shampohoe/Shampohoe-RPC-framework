package com.shampohoe.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ClassName:RpcHeartBeat
 * Package:com.shampohoe.rpc.entity
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/19 13:05
 * #Version 1.1
 */
@Data
public class RpcHeartBeat implements Serializable {
    private final String HEART="HEART BEAT";
}
