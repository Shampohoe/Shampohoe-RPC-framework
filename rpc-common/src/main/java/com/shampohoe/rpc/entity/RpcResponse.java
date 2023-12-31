package com.shampohoe.rpc.entity;

import com.shampohoe.rpc.enums.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ClassName:RpcResponse
 * Package:com.shampohoe.rpc.entity
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/12 20:58
 * #Version 1.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse<T>implements Serializable {
    /**
     * 响应对应的请求号
     */
    private String requestId;
    /**
     *响应状态码
     */
    private Integer statusCode;
    /**
     *响应状态码对应的信息
     */
    private String message;
    /**
     *成功时的响应数据
     */
    private T data;


    /**
     * @description 成功时服务端返回的对象
     * @param [data]
     * @return [com.panda.rpc.entity.RpcResponse<T>]
     */
    public static <T> RpcResponse<T> success(T data,String requestId){
        RpcResponse<T> response = new RpcResponse();
        response.setRequestId(requestId);
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    /**
     * @description 失败时服务端返回的对象
     * @param [code]
     * @return [com.panda.rpc.entity.RpcResponse<T>]
     */
    public static <T> RpcResponse<T> fail(ResponseCode code,String requestId){
        RpcResponse<T> response = new RpcResponse();
        response.setRequestId(requestId);
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}
