package rpc.socket.client;

import com.shampohoe.rpc.entity.RpcRequest;
import com.shampohoe.rpc.entity.RpcResponse;
import com.shampohoe.rpc.enums.ResponseCode;
import com.shampohoe.rpc.enums.RpcError;
import com.shampohoe.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import rpc.client.RpcClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * ClassName:RpcClient
 * Package:rpc.client
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/12 21:25
 * #Version 1.1
 */
@Slf4j
public class SocketClient implements RpcClient {

    private final String host;
    private final int port;

    public SocketClient(String host, int port){
        this.host = host;
        this.port = port;
    }
    public Object sendRequest(RpcRequest rpcRequest) {
        /**
         * socket套接字实现TCP网络传输
         * try()中一般放对资源的申请，若{}出现异常，()资源会自动关闭
         */
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            RpcResponse rpcResponse = (RpcResponse)objectInputStream.readObject();
            if(rpcResponse == null){
                log.error("服务调用失败，service:{}" + rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, "service:" + rpcRequest.getInterfaceName());
            }
            if(rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()){
                log.error("服务调用失败，service:{} response:{}", rpcRequest.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, "service:" + rpcRequest.getInterfaceName());
            }
            return rpcResponse.getData();
        } catch (IOException | ClassNotFoundException e) {
            log.error("调用时有错误发生：" + e);
            throw new RpcException("服务调用失败：", e);
        }
    }
}
