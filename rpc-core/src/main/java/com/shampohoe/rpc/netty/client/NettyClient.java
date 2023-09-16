package com.shampohoe.rpc.netty.client;

import com.shampohoe.rpc.client.RpcClient;
import com.shampohoe.rpc.codec.CommonDecoder;
import com.shampohoe.rpc.codec.CommonEncoder;
import com.shampohoe.rpc.entity.RpcRequest;
import com.shampohoe.rpc.entity.RpcResponse;
import com.shampohoe.rpc.enums.RpcError;
import com.shampohoe.rpc.exception.RpcException;
import com.shampohoe.rpc.factory.SingletonFactory;
import com.shampohoe.rpc.registry.ServiceRegistry;
import com.shampohoe.rpc.registry.ZkServiceRegistry;
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
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
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
    private static final EventLoopGroup group;
    private static final Bootstrap bootstrap;

    static {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        // 1. 指定线程模型
        bootstrap.group(group)
                // 2. 指定IO类型为NIO
                .channel(NioSocketChannel.class);
    }

    private final ServiceRegistry serviceDiscovery;
    private final CommonSerializer serializer;
    private final UnprocessedRequests unprocessedRequests;

    public NettyClient() {
        this(DEFAULT_SERIALIZER);
    }

    public NettyClient(Integer serializer) {
        // 初始化注册中心，建立连接
        this.serviceDiscovery = new ZkServiceRegistry();
        this.serializer = CommonSerializer.getByCode(serializer);
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    @Override
    public CompletableFuture<RpcResponse> sendRequest(RpcRequest rpcRequest) {
        if(serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }

        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();

        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.serviceDiscovery(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);

            if (!channel.isActive()) {
                group.shutdownGracefully();
                return null;
            }

            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);

            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    log.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                } else {
                    // 为了让netty不会关闭
                    future1.channel().close();
                    resultFuture.completeExceptionally(future1.cause());
                    log.error("发送消息时有错误发生: ", future1.cause());
                }
            });
        } catch (InterruptedException e) {
            unprocessedRequests.remove(rpcRequest.getRequestId());
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }

        return resultFuture;
    }

}
