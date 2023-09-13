package rpc.socket.server;

import com.shampohoe.rpc.entity.RpcRequest;
import com.shampohoe.rpc.entity.RpcResponse;
import lombok.extern.slf4j.Slf4j;
import rpc.client.RequestHandler;
import rpc.registry.ServiceRegistry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * ClassName:RequestHandlerThread
 * Package:rpc.server
 * Description:  IO传输模式|处理客户端RpcRequest的工作线程
 *
 * @Author kkli
 * @Create 2023/9/13 1:00
 * #Version 1.1
 */
@Slf4j
public class RequestHandlerThread implements  Runnable{
    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceRegistry serviceRegistry;
    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try(ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest)objectInputStream.readObject();
            String interfaceName = rpcRequest.getInterfaceName();

            Object service = serviceRegistry.getService(interfaceName);
            Object result = requestHandler.handle(rpcRequest, service);

            objectOutputStream.writeObject(RpcResponse.success(result));
            objectOutputStream.flush();
        }catch (IOException | ClassNotFoundException e){
            log.info("调用或发送时发生错误：" + e);
        }
    }
}
