package com.zj.demoplugin.enums;

/**
 * @author 19242
 */
public enum NoticeEnum {
    /**
     * json错误
     */
    JSON_ERROR("Json解析错误", "请检查JsonArray是否正确"),
    /**
     * sql错误
     */
    SQL_ERROR("SQL解析错误", "请检查SQL，支持：查询、等值、like、null、in、<>、范围、排序、limit；不支持：insert等、union、group、函数");


    private final String warn;
    private final String message;

    NoticeEnum(String warn, String message) {
        this.warn = warn;
        this.message = message;
    }

    public String getWarn() {
        return warn;
    }

    public String getMessage() {
        return message;
    }
}
