package com.shampohoe.rpc.test;

import com.shampohoe.rpc.api.HelloObject;
import com.shampohoe.rpc.api.HelloService;
import lombok.extern.slf4j.Slf4j;


/**
 * ClassName:HelloServiceImpl
 * Package:com.shampohoe.rpc.test
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/12 20:41
 * #Version 1.1
 */
@Slf4j
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(HelloObject object) {
        //使用{}可以直接将getMessage()内容输出
        log.info("接收到：{}", object.getMessage());
        return "这是调用的返回值：id=" + object.getId();
    }
}

