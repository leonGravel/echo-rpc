package com.gravel.echo.common.utils;

import com.alibaba.fastjson.JSON;
import com.gravel.echo.common.codec.kryo.KryoSerializer;
import com.gravel.echo.common.constants.EchoConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName SerializerUtil
 * @Description: TODO
 * @Author gravel
 * @Date 2019/12/3
 * @Version V1.0
 **/
@Component
public class SerializerUtil {

    private static String serialType;

    public static <T> T parseObject(String text, Class<T> clazz) {
        if(EchoConstants.SERIAL_TYPE_JSON.endsWith(serialType)){
            return JSON.parseObject(text, clazz);
        }else{
            return KryoSerializer.parseObject(text, clazz);
        }
    }

    @Value("${echo.serial.type}")
    public void setSerialType(String serialType) {
        System.out.println(serialType);
        SerializerUtil.serialType = serialType;
    }
}
