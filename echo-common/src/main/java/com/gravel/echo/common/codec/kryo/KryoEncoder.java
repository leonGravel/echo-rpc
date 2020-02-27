package com.gravel.echo.common.codec.kryo;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @ClassName KryoEncoder
 * @Description: 加密
 * @Author gravel
 * @Date 2019/12/3
 * @Version V1.0
 **/
public class KryoEncoder extends MessageToMessageEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byte[] bytes;
        bytes = KryoSerializer.serialize(msg);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        out.add(byteBuf);
    }
}
