package com.gravel.echo.common.entity;

import lombok.Data;

/**
 * @ClassName Request
 * @Description: 客户端请求实体类
 * @Author gravel
 * @Date 2019/11/28
 * @Version V1.0
 **/

@Data
public class Request {
    private String id;
    /**
     * 类名
     */
    private String className;
    /**
     * 远程调用函数名称
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 参数列表
     */
    private Object[] parameters;
}
