package com.gravel.echo.client;

import com.alibaba.fastjson.JSON;
import com.gravel.echo.client.conection.ConnectManage;
import com.gravel.echo.common.entity.Request;
import com.gravel.echo.common.entity.Response;
import com.gravel.echo.common.utils.SerializerUtil;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

/**
 * @ClassName NettyClientHandler
 * @Description: TODO
 * @Author gravel
 * @Date 2019/11/28
 * @Version V1.0
 **/
@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<Response> {

    @Autowired
    NettyClient client;

    @Autowired
    ConnectManage connectManage;


    private ConcurrentHashMap<String, SynchronousQueue<Response>> queueMap = new ConcurrentHashMap<>();

    public void channelActive(ChannelHandlerContext ctx) {
        log.info("已连接到RPC服务器.{}", ctx.channel().remoteAddress());
    }

    public void channelInactive(ChannelHandlerContext ctx) {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        log.info("与RPC服务器断开连接." + address);
        ctx.channel().close();
        connectManage.removeChannel(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response response) throws Exception {
        String requestId = response.getRequestId();
        SynchronousQueue<Response> queue = queueMap.get(requestId);
        queue.put(response);
        queueMap.remove(requestId);
    }


    public SynchronousQueue<Response> sendRequest(Request request, Channel channel) {
        SynchronousQueue<Response> queue = new SynchronousQueue<>();
        queueMap.put(request.getId(), queue);
        channel.writeAndFlush(request);
        return queue;
    }


    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("已超过30秒未与RPC服务器进行读写操作!将发送心跳消息...");
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                Request request = new Request();
                request.setMethodName("heartBeat");
                ctx.channel().writeAndFlush(request);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("RPC通信服务器发生异常.{}", cause);
        ctx.channel().close();
    }



}
