package com.shampohoe.rpc.netty.server;

import com.shampohoe.rpc.client.RequestHandler;
import com.shampohoe.rpc.entity.RpcMessage;
import com.shampohoe.rpc.entity.RpcRequest;
import com.shampohoe.rpc.entity.RpcResponse;
import com.shampohoe.rpc.enums.PackageType;
import com.shampohoe.rpc.factory.SingletonFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:NettyServerHandler
 * Package:rpc.netty.server
 * Description: Netty中处理从客户端传来的RpcRequest
 *
 * @Author kkli
 * @Create 2023/9/13 14:34
 * #Version 1.1
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcMessage> {
    private static RequestHandler requestHandler;


    public NettyServerHandler() {
        this.requestHandler = SingletonFactory.getInstance(RequestHandler.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) throws Exception {
        try {
            log.info("服务器接收到请求: {}", msg);
            if (msg instanceof RpcMessage) {
                int messageType = msg.getMessageType();
                if(messageType== PackageType.HEARTBEAT_PACK.getCode()){
                    log.info("heart [{}]", msg.getData());
                }else{
                    Object result = requestHandler.handle((RpcRequest) msg.getData());
                    if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                        RpcMessage rpcMessage = RpcMessage.builder().data(RpcResponse.success(result, msg.getRequestId()))
                                .requestId(msg.getRequestId())
                                .messageType(PackageType.RESPONSE_PACK.getCode()).build();
                        // 向客户端返回响应数据
                        ctx.writeAndFlush(rpcMessage);
                    } else {
                        log.error("通道不可写");
                    }
                }
            }
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    //心跳检测
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {

                ChannelFuture closeFuture=ctx.channel().close();
                closeFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        log.info("idle check happen, so close the connection");
                    }
                });

            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生：");
        cause.printStackTrace();
        ctx.close();
    }
}
