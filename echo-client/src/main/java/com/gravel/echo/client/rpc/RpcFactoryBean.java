package com.gravel.echo.client.rpc;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;

/**
 * @ClassName RpcFactoryBean
 * @Description: TODO
 * @Author gravel
 * @Date 2019/11/28
 * @Version V1.0
 **/
public class RpcFactoryBean<T> implements FactoryBean<T> {

    private Class<T> rpcInterface;

    @Autowired
    RpcFactory<T> factory;

    public RpcFactoryBean() {}

    public RpcFactoryBean(Class<T> rpcInterface) {
        this.rpcInterface = rpcInterface;
    }

    public T getObject() throws Exception {
        return getRpc();
    }

    public Class<?> getObjectType() {
        return this.rpcInterface;
    }

    public boolean isSingleton() {
        return true;
    }

    private <T> T getRpc() {
        return (T) Proxy.newProxyInstance(rpcInterface.getClassLoader(), new Class[] { rpcInterface },factory);
    }
}
