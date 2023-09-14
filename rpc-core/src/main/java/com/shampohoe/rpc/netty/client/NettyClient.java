package com.shampohoe.rpc.netty.client;

import com.shampohoe.rpc.client.RpcClient;
import com.shampohoe.rpc.codec.CommonDecoder;
import com.shampohoe.rpc.codec.CommonEncoder;
import com.shampohoe.rpc.entity.RpcRequest;
import com.shampohoe.rpc.entity.RpcResponse;
import com.shampohoe.rpc.enums.RpcError;
import com.shampohoe.rpc.exception.RpcException;
import com.shampohoe.rpc.serializer.CommonSerializer;
import com.shampohoe.rpc.serializer.JsonSerializer;
import com.shampohoe.rpc.serializer.KryoSerializer;
import com.shampohoe.rpc.util.RpcMessageChecker;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.bootstrap.Bootstrap;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;


/**
 * ClassName:NettyClient
 * Package:rpc.netty.client
 * Description: Netty方式客户端
 *
 * @Author kkli
 * @Create 2023/9/13 14:32
 * #Version 1.1
 */
@Slf4j
public class NettyClient implements RpcClient {
    private String host;
    private int port;
    private CommonSerializer serializer;

    public NettyClient(String host, int port){
        this.host = host;
        this.port = port;
    }


    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        //保证自定义实体类变量的原子性和共享性的线程安全，此处应用于rpcResponse
        AtomicReference<Object> result = new AtomicReference<>(null);
        try {
            Channel channel = ChannelProvider.get(new InetSocketAddress(host, port), serializer);
            if(channel.isActive()){
                //向服务端发请求，并设置监听，关于writeAndFlush()的具体实现可以参考：https://blog.csdn.net/qq_34436819/article/details/103937188
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if(future1.isSuccess()){
                        log.info(String.format("客户端发送消息：%s", rpcRequest.toString()));
                    }else {
                        log.error("发送消息时有错误发生:", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                //AttributeMap<AttributeKey, AttributeValue>是绑定在Channel上的，可以设置用来获取通道对象
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + rpcRequest.getRequestId());
                //get()阻塞获取value
                RpcResponse rpcResponse = channel.attr(key).get();
                RpcMessageChecker.check(rpcRequest, rpcResponse);
                result.set(rpcResponse.getData());
            }else {
            //0表示”正常“退出程序，即如果当前程序还有在执行的任务，则等待所有任务执行完成以后再退出
            System.exit(0);
            }
        }catch (InterruptedException e){
            log.error("发送消息时有错误发生:", e);
        }
        return result.get();
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
