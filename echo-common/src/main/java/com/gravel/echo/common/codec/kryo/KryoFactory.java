package com.gravel.echo.common.codec.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.gravel.echo.common.entity.Request;
import com.gravel.echo.common.entity.Response;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;

import java.util.Arrays;

/**
 * @ClassName KryoFactory
 * @Description: TODO
 * @Author gravel
 * @Date 2019/12/3
 * @Version V1.0
 **/
public abstract class KryoFactory {

    private final static KryoFactory threadFactory = new ThreadLocalKryoFactory();

    protected KryoFactory() {

    }

    public static KryoFactory getDefaultFactory() {
        return threadFactory;
    }

    protected Kryo createKryo() {

        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.register(Request.class);
        kryo.register(Response.class);

        UnmodifiableCollectionsSerializer.registerSerializers(kryo);
        SynchronizedCollectionsSerializer.registerSerializers(kryo);
        kryo.register(StringBuffer.class);
        kryo.register(StringBuilder.class);
        kryo.register(Object.class);
        kryo.register(Object[].class);
        kryo.register(String[].class);
        kryo.register(byte[].class);
        kryo.register(char[].class);
        kryo.register(int[].class);
        kryo.register(float[].class);
        kryo.register(double[].class);

        return kryo;
    }
}
