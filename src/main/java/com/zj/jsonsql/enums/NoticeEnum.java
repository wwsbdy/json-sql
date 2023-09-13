package com.zj.jsonsql.enums;

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
    SQL_ERROR("SQL解析错误", "<html>请检查SQL，支持：查询、去重、等值、like、null、in、范围、排序、limit；<br>不支持：insert等、union、group、函数；<br>result、date等要加上``</html>"),
    /**
     * Json过长
     */
    ROWS_TOO_MANY("Json过长", "数据行过长"),
    /**
     * Json过长
     */
    COLUMNS_TOO_MANY("Json过长", "数据列过长"),
    /**
     * SQL过长
     */
    SQL_TOO_LONG("SQL过长", "SQL过于复杂"),
    ;

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
