package com.spike.demo.util;

/**
 * @ClassName ResultEnum
 * @Description 接口统一返回的code、message，方便维护
 * @Author simonsfan
 * @Date 2018/12/18
 * Version  1.0
 */
public enum  ResultEnum {
    SUCCESS(200,"success"),
    FAIL(500,"system error"),
    FREQUENT(-1,"too frequent visit"),
    SPIKEFAIL(1001,"no rest stock");

    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
