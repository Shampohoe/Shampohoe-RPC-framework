package com.shampohoe.rpc.netty.client;

import com.shampohoe.rpc.client.RpcClient;
import com.shampohoe.rpc.entity.RpcMessage;
import com.shampohoe.rpc.entity.RpcRequest;
import com.shampohoe.rpc.entity.RpcResponse;
import com.shampohoe.rpc.enums.PackageType;
import com.shampohoe.rpc.enums.RpcError;
import com.shampohoe.rpc.exception.RpcException;
import com.shampohoe.rpc.factory.SingletonFactory;
import com.shampohoe.rpc.loadbalance.LoadBalancer;
import com.shampohoe.rpc.loadbalance.loadbalancer.RandomLoadBalance;
import com.shampohoe.rpc.registry.ServiceDiscovery;
import com.shampohoe.rpc.registry.zk.ZKServiceDiscoveryImpl;
import com.shampohoe.rpc.serializer.CommonSerializer;

import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.bootstrap.Bootstrap;
import lombok.extern.slf4j.Slf4j;
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
    public NettyClient() {
        this(DEFAULT_SERIALIZER, new RandomLoadBalance());
    }

    public NettyClient(LoadBalancer loadBalancer) {
        this(DEFAULT_SERIALIZER, loadBalancer);
    }

    public NettyClient(Integer serializer) {
        this(serializer, new RandomLoadBalance());
    }

    private final ServiceDiscovery serviceDiscovery;
    private final CommonSerializer serializer;
    private final UnprocessedRequests unprocessedRequests;

    public CommonSerializer getSerializer(){
        return serializer;
    }

    public NettyClient(Integer serializer, LoadBalancer loadBalancer) {
        // 初始化注册中心，建立连接
        this.serviceDiscovery =  new ZKServiceDiscoveryImpl(loadBalancer);
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
            //将request封装成rpcMessage
            RpcMessage rpcMessage = RpcMessage.builder().data(rpcRequest)
                    .requestId(rpcRequest.getRequestId())
                    .messageType(PackageType.REQUEST_PACK.getCode()).build();

            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future1 -> {
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
