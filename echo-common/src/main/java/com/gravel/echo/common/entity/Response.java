package com.gravel.echo.common.entity;

import lombok.Data;

/**
 * @ClassName Response
 * @Description: 服务端返回实体类
 * @Author gravel
 * @Date 2019/11/28
 * @Version V1.0
 **/

@Data
public class Response {
    /**
     * 对应客户端请求ID
     */
    private String requestId;
    /**
     * 服务端返回状态码
     */
    private int code;
    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 服务端返回信息
     */
    private Object data;
}
