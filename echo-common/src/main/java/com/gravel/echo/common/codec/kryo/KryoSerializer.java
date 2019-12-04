package com.gravel.echo.common.codec.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @ClassName KryoSerializer
 * @Description: TODO
 * @Author gravel
 * @Date 2019/12/3
 * @Version V1.0
 **/
@Slf4j
public class KryoSerializer {
    private static final ThreadLocalKryoFactory factory = new ThreadLocalKryoFactory();

    public static void serialize(Object object, ByteBuf out) {
        Kryo kryo = factory.getKryo();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeClassAndObject(output, object);
        output.flush();
        output.close();

        byte[] b = baos.toByteArray();
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            log.error("kyro 序列化失败:{0}", e);
        }
        out.writeBytes(b);
    }

    public static Object deserialize(ByteBuf out) {
        if (out == null) {
            return null;
        }
        Input input = new Input(new ByteBufInputStream(out));
        Kryo kryo = factory.getKryo();
        return kryo.readClassAndObject(input);
    }

}
