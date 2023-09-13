package com.shampohoe.rpc.netty.server;

import com.shampohoe.rpc.client.RequestHandler;
import com.shampohoe.rpc.entity.RpcRequest;
import com.shampohoe.rpc.entity.RpcResponse;
import com.shampohoe.rpc.registry.DefaultServiceRegistry;
import com.shampohoe.rpc.registry.ServiceRegistry;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
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
    private static ServiceRegistry serviceRegistry;

    static{
        requestHandler = new RequestHandler();
        serviceRegistry = new DefaultServiceRegistry();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try{
            log.info("服务端接收到请求：{}", msg);
            String interfaceName = msg.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result = requestHandler.handle(msg, service);
            ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result));
            //添加一个监听器到channelfuture来检测是否所有的数据包都发出，然后关闭通道
            future.addListener(ChannelFutureListener.CLOSE);
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
