package com.shampohoe.rpc.netty.client;

import com.shampohoe.rpc.entity.RpcResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName:UnprocessRequest
 * Package:com.shampohoe.rpc.netty.client
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/16 3:35
 * #Version 1.1
 */
public class UnprocessedRequests {

    private static ConcurrentHashMap<String, CompletableFuture<RpcResponse>> unprocessedResponseFutures = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<RpcResponse> future) {
        unprocessedResponseFutures.put(requestId, future);
    }

    public void remove(String requestId) {
        unprocessedResponseFutures.remove(requestId);
    }

    public void complete(RpcResponse rpcResponse) {
        CompletableFuture<RpcResponse> future = unprocessedResponseFutures.remove(rpcResponse.getRequestId());
        if (null != future) {
            future.complete(rpcResponse);
        } else {
            throw new IllegalStateException();
        }
    }
}
