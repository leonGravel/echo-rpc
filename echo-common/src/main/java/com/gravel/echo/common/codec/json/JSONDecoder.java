package com.gravel.echo.common.codec.json;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @ClassName JSONDecoder
 * @Description: 使用定长解码器，解决粘包拆包问题
 * @Author gravelØ
 * @Date 2019/11/28
 * @Version V1.0
 **/
public class JSONDecoder extends LengthFieldBasedFrameDecoder {
    public JSONDecoder() {
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
        return JSON.parse(bytes);
    }
}
