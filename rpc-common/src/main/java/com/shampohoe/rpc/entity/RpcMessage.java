package com.shampohoe.rpc.entity;


import lombok.*;

/**
 * @author wangtao
 * @createTime 2020年10月2日 12:33
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcMessage {

    /**
     * rpc message type
     */
    private int messageType;

    /**
     * request id
     */
    private String requestId;
    /**
     * request data
     */
    private Object data;

}
