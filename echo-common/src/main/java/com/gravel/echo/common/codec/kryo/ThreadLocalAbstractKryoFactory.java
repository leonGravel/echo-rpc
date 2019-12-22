package com.gravel.echo.common.codec.kryo;

import com.esotericsoftware.kryo.Kryo;

/**
 * @ClassName ThreadLocalKryoFactory
 * @Description: 使用threadlocal实现kyro对象重用
 * @Author gravel
 * @Date 2019/12/3
 * @Version V1.0
 **/
public class ThreadLocalAbstractKryoFactory extends AbstractKryoFactory {

    private final ThreadLocal<Kryo> holder  = ThreadLocal.withInitial(this::createKryo);

    public Kryo getKryo() {
        return holder.get();
    }
}
