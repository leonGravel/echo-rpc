package com.gravel.echo.common.entity;

import lombok.Data;

/**
 * @ClassName Response
 * @Description: TODO
 * @Author gravel
 * @Date 2019/11/28
 * @Version V1.0
 **/

@Data
public class Response {
    private String requestId;
    private int code;
    private String errorMsg;
    private Object data;
}
