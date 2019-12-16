package com.gravel.echo.client.rpc;

import com.gravel.echo.client.NettyClient;
import com.gravel.echo.common.entity.Request;
import com.gravel.echo.common.entity.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @ClassName RpcFactory
 * @Description: RPC 代理生成类
 * @Author gravel
 * @Date 2019/11/28
 * @Version V1.0
 **/
@Slf4j
@Component
public class RpcFactory<T> implements MethodInterceptor {

    @Autowired
    NettyClient client;


    public T createCglibProxy(Class<?> clazz) {
        // cglib 中常用的一个类，可以代理类和接口
        Enhancer enhancer = new Enhancer();
        // 设置他所代理的类，cglib会动态生成他的子类字节码，通过继承这个类，实现代理
        enhancer.setSuperclass(clazz);
        // 设置代理方法拦截
        enhancer.setCallback(this);
        // 返回这个代理类
        return (T) enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Request request = new Request();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setParameterTypes(method.getParameterTypes());
        request.setId(UUID.randomUUID().toString());

        Response response = client.send(request);

        if (response.getCode()==1){
            throw new Exception(response.getErrorMsg());
        }
        return response.getData();
    }
}