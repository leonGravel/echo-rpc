package com.gravel.echo.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gravel.echo.common.entity.Request;
import com.gravel.echo.common.entity.Response;
import com.gravel.echo.common.utils.SerializerUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * @ClassName NettyServerHandler
 * @Description: TODO
 * @Author gravel
 * @Date 2019/11/28
 * @Version V1.0
 **/
@Slf4j
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, Object> serviceMap;

    NettyServerHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("客户端连接成功!" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("客户端断开连接!{}", ctx.channel().remoteAddress());
        ctx.channel().close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Request request = SerializerUtil.parseObject(msg.toString(), Request.class);

        if ("heartBeat".equals(request.getMethodName())) {
            log.info("客户端心跳信息..." + ctx.channel().remoteAddress());
        } else {
            log.info("RPC客户端请求接口:" + request.getClassName() + "   方法名:" + request.getMethodName());
            Response response = new Response();
            response.setRequestId(request.getId());
            try {
                Object result = this.handler(request);
                response.setData(result);
            } catch (Throwable e) {
                e.printStackTrace();
                response.setCode(1);
                response.setErrorMsg(e.toString());
                log.error("RPC Server handle request error", e);
            }
            ctx.writeAndFlush(response);
        }
    }

    /**
     * 通过反射，执行本地方法
     *
     * @param request
     * @return
     * @throws Throwable
     */
    private Object handler(Request request) throws Throwable {
        String className = request.getClassName();
        Object serviceBean = serviceMap.get(className);

        if (serviceBean != null) {
            Class<?> serviceClass = serviceBean.getClass();
            String methodName = request.getMethodName();
            Class<?>[] parameterTypes = request.getParameterTypes();
            Object[] parameters = request.getParameters();

            Method method = serviceClass.getMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(serviceBean, getParameters(parameterTypes, parameters));
        } else {
            throw new Exception("未找到服务接口,请检查配置!:" + className + "#" + request.getMethodName());
        }
    }

    /**
     * 获取参数列表
     *
     * @param parameterTypes
     * @param parameters
     * @return
     */
    private Object[] getParameters(Class<?>[] parameterTypes, Object[] parameters) {
        if (parameters == null || parameters.length == 0) {
            return parameters;
        } else {
            Object[] newParameters = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Class<?> paramType = parameterTypes[i];

                // 类型判断，转为不同的对象
                if (paramType.isPrimitive() || String.class.isAssignableFrom(paramType)) {
                    newParameters[i] = parameters[i];
                } else if (Collection.class.isAssignableFrom(paramType)) {
                    newParameters[i] = JSONArray.parseArray(parameters[i].toString(), Object.class);
                } else if (Map.class.isAssignableFrom(paramType)) {
                    newParameters[i] = SerializerUtil.parseObject(parameters[i].toString(), Map.class);
                } else {
                    newParameters[i] = SerializerUtil.parseObject(parameters[i].toString(), parameterTypes[i]);
                }
            }
            return newParameters;
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                log.info("客户端已超过60秒未读写数据,关闭连接.{}", ctx.channel().remoteAddress());
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info(cause.getMessage());
        ctx.close();
    }


}