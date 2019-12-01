package com.gravel.echo.example.service;

import com.gravel.echo.common.annotation.RpcService;

/**
 * @ClassName HelloServiceImpl
 * @Description: TODO
 * @Author gravel
 * @Date 2019/11/28
 * @Version V1.0
 **/
@RpcService
public class HelloServiceImpl implements HelloService {

    @Override
    public String saySomething(String something) {
        System.out.println("调用成功：---" + something);
        return something;
    }
}
