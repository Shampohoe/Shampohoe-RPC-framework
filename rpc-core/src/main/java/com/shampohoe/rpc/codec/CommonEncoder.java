package com.shampohoe.rpc.codec;

import com.esotericsoftware.minlog.Log;
import com.shampohoe.rpc.entity.RpcMessage;
import com.shampohoe.rpc.entity.RpcRequest;
import com.shampohoe.rpc.enums.PackageType;
import com.shampohoe.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:CommonEncoder
 * Package:rpc.codec
 * Description: 通用编码拦截器
 *
 * @Author kkli
 * @Create 2023/9/13 14:40
 * #Version 1.1
 */
@Slf4j
public class CommonEncoder extends MessageToByteEncoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer){
        this.serializer = serializer;
    }

    // 自定义传输协议,防止粘包
    // 消息格式为: [魔数][数据包类型][序列化器类型][数据长度][数据]
    //			  4字节   4字节      4字节       4字节
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        //数据写到缓冲区
        log.info("this is encoder");
        RpcMessage msgc=(RpcMessage)msg;
        out.writeInt(MAGIC_NUMBER);
        if(msgc.getMessageType()==PackageType.REQUEST_PACK.getCode()){
            out.writeInt(PackageType.REQUEST_PACK.getCode());
        }else if(msgc.getMessageType()==PackageType.RESPONSE_PACK.getCode()){
            out.writeInt(PackageType.RESPONSE_PACK.getCode());
        }else{
            out.writeInt(PackageType.HEARTBEAT_PACK.getCode());
        }
        out.writeInt(serializer.getCode());
        byte[] bytes = serializer.serialize(msgc);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
