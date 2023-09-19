package com.shampohoe.rpc.netty.client;

import com.shampohoe.rpc.entity.RpcHeartBeat;
import com.shampohoe.rpc.entity.RpcMessage;
import com.shampohoe.rpc.entity.RpcRequest;
import com.shampohoe.rpc.entity.RpcResponse;
import com.shampohoe.rpc.enums.PackageType;
import com.shampohoe.rpc.factory.SingletonFactory;
import com.shampohoe.rpc.serializer.CommonSerializer;
import com.shampohoe.rpc.serializer.KryoSerializer;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

/**
 * ClassName:NettyClientHandler
 * Package:rpc.netty.client
 * Description:客户端Netty处理器
 *
 * @Author kkli
 * @Create 2023/9/13 14:33
 * #Version 1.1
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcMessage> {

    private final UnprocessedRequests unprocessedRequests;
    private final CommonSerializer serializer;

    public NettyClientHandler(CommonSerializer serializer) {
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.serializer=serializer;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) {
        try {
            log.info(String.format("客户端接收到消息：%s", msg));
            if(msg instanceof RpcMessage) {
                int messageType = msg.getMessageType();
                if(messageType== PackageType.HEARTBEAT_PACK.getCode()){
                    log.info("heart [{}]", msg.getData());
                }else if (messageType== PackageType.RESPONSE_PACK.getCode()){
                    RpcResponse<Object> rpcResponse = (RpcResponse<Object>) msg.getData();
                    unprocessedRequests.complete(rpcResponse);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                log.info("write idle happen [{}]", ctx.channel().remoteAddress());
                InetSocketAddress socketAddress=(InetSocketAddress )ctx.channel().remoteAddress();
                Channel channel = ChannelProvider.get(socketAddress,serializer);
                RpcMessage rpcMessage = new RpcMessage();
                rpcMessage.setMessageType(PackageType.HEARTBEAT_PACK.getCode());
                rpcMessage.setRequestId(UUID.randomUUID().toString());
                rpcMessage.setData(new RpcHeartBeat());
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("过程调用中有错误发生：");
        cause.printStackTrace();
        ctx.close();
    }
}
