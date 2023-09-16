package com.shampohoe.rpc.netty.client;

import com.shampohoe.rpc.entity.RpcResponse;
import com.shampohoe.rpc.factory.SingletonFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

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
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private final UnprocessedRequests unprocessedRequests;

    public NettyClientHandler() {
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        try {
            log.info(String.format("客户端接收到消息：%s", msg));
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + msg.getRequestId());
            ctx.channel().attr(key).set(msg);
            //关闭客户端通道
            ctx.channel().close();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("过程调用中有错误发生：");
        cause.printStackTrace();
        ctx.close();
    }
}
