package com.shampohoe.rpc.codec;

import com.shampohoe.rpc.entity.RpcRequest;
import com.shampohoe.rpc.entity.RpcResponse;
import com.shampohoe.rpc.enums.PackageType;
import com.shampohoe.rpc.enums.RpcError;
import com.shampohoe.rpc.exception.RpcException;
import com.shampohoe.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

/**
 * ClassName:CommonDecoder
 * Package:rpc.codec
 * Description:  通用的解码拦截器
 *
 * @Author kkli
 * @Create 2023/9/13 14:39
 * #Version 1.1
 */
@Slf4j
public class CommonDecoder extends ReplayingDecoder {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //从缓冲区中读取数据
        int magic = in.readInt();
        if(magic != MAGIC_NUMBER){
            log.error("不识别的协议包：{}", magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        int packageCode = in.readInt();
        Class<?> packageClass;
        if(packageCode == PackageType.REQUEST_PACK.getCode()){
            packageClass = RpcRequest.class;
        }else if (packageCode == PackageType.RESPONSE_PACK.getCode()){
            packageClass = RpcResponse.class;
        }else {
            log.error("不识别的数据包：{}", packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
        int serializerCode = in.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if(serializer == null){
            log.error("不识别的反序列化器：{}", serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        Object obj = serializer.deserialize(bytes, packageClass);
        //添加到对象列表
        out.add(obj);
    }
}
