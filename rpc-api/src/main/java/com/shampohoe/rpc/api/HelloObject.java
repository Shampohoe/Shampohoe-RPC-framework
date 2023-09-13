package com.shampohoe.rpc.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ClassName:HelloObject
 * Package:com.shampohoe.rpc.api
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/12 20:36
 * #Version 1.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelloObject implements Serializable {
    //Serializable序列化接口没有任何方法或者字段，只是用于标识可序列化的语义。
    //实现了Serializable接口的类可以被ObjectOutputStream转换为字节流。
    //同时也可以通过ObjectInputStream再将其解析为对象。
    //序列化是指把对象转换为字节序列的过程;反序列化则是把持久化的字节文件数据恢复为对象的过程。
    private Integer id;
    private String message;
}
