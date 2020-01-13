package com.gravel.echo.client;

import com.gravel.echo.client.conection.ConnectManage;
import com.gravel.echo.common.codec.kryo.KryoDecoder;
import com.gravel.echo.common.codec.kryo.KryoEncoder;
import com.gravel.echo.common.entity.Request;
import com.gravel.echo.common.entity.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.SocketAddress;
import java.util.concurrent.SynchronousQueue;

/**
 * @ClassName NettyClient
 * @Description: ECHO 客户端
 * @Author gravel
 * @Date 2019/11/28
 * @Version V1.0
 **/
@Component
@Slf4j
public class NettyClient {

    private EventLoopGroup group = new NioEventLoopGroup(1);
    private Bootstrap bootstrap = new Bootstrap();

    @Autowired
    NettyClientHandler clientHandler;

    @Autowired
    ConnectManage connectManage;


    public NettyClient() {
        bootstrap.group(group).
                channel(NioSocketChannel.class).
                option(ChannelOption.TCP_NODELAY, true).
                option(ChannelOption.SO_KEEPALIVE, true).
                handler(new ChannelInitializer<SocketChannel>() {
                    //创建NIOSocketChannel成功后，在进行初始化时，将它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        // 设置心跳
                        pipeline.addLast(new IdleStateHandler(0, 0, 30));
                        pipeline.addLast(new KryoDecoder());
                        pipeline.addLast(new KryoEncoder());
                        pipeline.addLast("handler", clientHandler);
                    }
                });
    }

    @PreDestroy
    public void destroy() {
        log.info("RPC客户端退出,释放资源!");
        group.shutdownGracefully();
    }

    /**
     * 发送远程调用请求
     * @param request
     * @return
     * @throws InterruptedException
     */
    public Response send(Request request) throws InterruptedException {

        Channel channel = connectManage.chooseChannel();
        if (channel != null && channel.isActive()) {
            SynchronousQueue<Response> queue = clientHandler.sendRequest(request, channel);
            return queue.take();
        } else {
            Response res = new Response();
            res.setCode(1);
            res.setErrorMsg("未正确连接到服务器.请检查相关配置信息!");
            return res;
        }
    }

    public Channel doConnect(SocketAddress address) throws InterruptedException {
        ChannelFuture future = bootstrap.connect(address);
        return future.sync().channel();
    }
}
