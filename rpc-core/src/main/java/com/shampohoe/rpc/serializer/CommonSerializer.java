package com.shampohoe.rpc.serializer;

/**
 * ClassName:CommonSerializer
 * Package:rpc.serializer
 * Description:  通用的序列化反序列化接口
 *
 * @Author kkli
 * @Create 2023/9/13 14:37
 * #Version 1.1
 */
public interface CommonSerializer {
    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

    Integer KRYO_SERIALIZER = 0;
    Integer JSON_SERIALIZER = 1;
    Integer DEFAULT_SERIALIZER = KRYO_SERIALIZER;

    static CommonSerializer getByCode(int code){
        switch (code){
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
