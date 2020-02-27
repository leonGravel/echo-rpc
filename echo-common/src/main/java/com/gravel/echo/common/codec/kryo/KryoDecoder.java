package com.gravel.echo.common.codec.kryo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @ClassName KryoDecoder
 * @Description: 使用定长解码器，解决粘包拆包问题
 * @Author gravel
 * @Date 2019/12/3
 * @Version V1.0
 **/
public class KryoDecoder extends LengthFieldBasedFrameDecoder {

    public KryoDecoder() {
        super(65535, 0, 4, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf decode = (ByteBuf) super.decode(ctx, in);
        if (decode == null) {
            return null;
        }
        int dataLen = decode.readableBytes();
        byte[] bytes = new byte[dataLen];
        decode.readBytes(bytes);
        return KryoSerializer.deserialize(bytes);
    }
}
