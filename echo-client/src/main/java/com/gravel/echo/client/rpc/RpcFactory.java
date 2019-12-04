package com.gravel.echo.client.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gravel.echo.client.NettyClient;
import com.gravel.echo.common.entity.Request;
import com.gravel.echo.common.entity.Response;
import com.gravel.echo.common.utils.SerializerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName RpcFactory
 * @Description: TODO
 * @Author gravel
 * @Date 2019/11/28
 * @Version V1.0
 **/
@Slf4j
@Component
public class RpcFactory<T> implements InvocationHandler {

    @Autowired
    NettyClient client;

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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