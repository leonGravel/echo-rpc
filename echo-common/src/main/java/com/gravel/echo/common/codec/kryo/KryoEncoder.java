package com.gravel.echo.common.codec.kryo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @ClassName KryoEncoder
 * @Description: 加密
 * @Author gravel
 * @Date 2019/12/3
 * @Version V1.0
 **/
public class KryoEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        KryoSerializer.serialize(msg, out);
        ctx.flush();
    }
}
