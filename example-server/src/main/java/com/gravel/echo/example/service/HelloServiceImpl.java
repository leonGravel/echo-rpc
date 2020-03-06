package com.gravel.echo.example.service;

import com.gravel.echo.common.annotation.RpcService;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<String> saySomething(List<String> something) {
        System.out.println("调用成功：---" + something);
        return something.stream().map(e -> e = "rpc--调用成功！" + e).collect(Collectors.toList());
    }
}
