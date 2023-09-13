package com.shampohoe.rpc.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shampohoe.rpc.entity.RpcRequest;
import com.shampohoe.rpc.enums.SerializerCode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * ClassName:JsonSerializer
 * Package:rpc.serializer
 * Description:  使用Json格式的序列化器
 *
 * @Author kkli
 * @Create 2023/9/13 14:38
 * #Version 1.1
 */
@Slf4j
public class JsonSerializer implements CommonSerializer{
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        try{
            return objectMapper.writeValueAsBytes(obj);
        }catch (JsonProcessingException e){
            log.error("序列化时有错误发生：{}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try{
            Object obj = objectMapper.readValue(bytes, clazz);
            if(obj instanceof RpcRequest){
                obj = handleRequest(obj);
            }
            return obj;
        }catch (IOException e){
            log.error("反序列化时有错误发生：{}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @description 由于使用JSON序列化和反序列化Object数组，无法保证反序列化后仍然为原实例类，需要重新判断处理
     * 因为都是Object类型，分不清
     * @param [obj]
     * @return [java.lang.Object]
     */
    private Object handleRequest(Object obj) throws IOException {
        RpcRequest rpcRequest = (RpcRequest) obj;
        for(int i = 0; i < rpcRequest.getParamTypes().length; i++){
            Class<?> clazz = rpcRequest.getParamTypes()[i];
            if(!clazz.isAssignableFrom(rpcRequest.getParameters()[i].getClass())){
                byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getParameters()[i]);
                rpcRequest.getParameters()[i] = objectMapper.readValue(bytes, clazz);
            }
        }
        return rpcRequest;
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("JSON").getCode();
    }
}
