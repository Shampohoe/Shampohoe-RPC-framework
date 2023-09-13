package rpc.client;

import com.shampohoe.rpc.entity.RpcRequest;

/**
 * ClassName:RpcClient
 * Package:rpc.client
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/13 12:31
 * #Version 1.1
 */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}
