package com.shampohoe.rpc.netty.server;

import com.shampohoe.rpc.client.RequestHandler;
import com.shampohoe.rpc.entity.RpcRequest;
import com.shampohoe.rpc.entity.RpcResponse;
import com.shampohoe.rpc.factory.SingletonFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static RequestHandler requestHandler;


    public NettyServerHandler() {
        this.requestHandler = SingletonFactory.getInstance(RequestHandler.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try {
            log.info("服务器接收到请求: {}", msg);
            Object result = requestHandler.handle(msg);
            if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                // 向客户端返回响应数据
                ctx.writeAndFlush(RpcResponse.success(result, msg.getRequestId()));
            } else {
                log.error("通道不可写");
            }
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生：");
        cause.printStackTrace();
        ctx.close();
    }
}
