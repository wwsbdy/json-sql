package com.zj.jsonsql.enums;

import lombok.Getter;

/**
 * @author 19242
 */
@Getter
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
    /**
     * 输入json为空
     */
    JSON_EMPTY("Json为空", "输入Json为空"),
    /**
     * 文件夹路径为空
     */
    FOLDER_EMPTY("文件夹路径为空", "文件夹路径为空"),
    /**
     * 文件类型错误
     */
    FILE_TYPE_ERROR("文件类型错误", "文件类型错误"),
    /**
     * 文件名错误
     */
    FILE_NAME_ERROR("文件名错误", "文件名错误"),
    /**
     * 文件导出失败
     */
    FILE_EXPORT_FAIL("文件导出失败", "请检查文件夹、文件名或json"),
    ;

    private final String warn;
    private final String message;

    NoticeEnum(String warn, String message) {
        this.warn = warn;
        this.message = message;
    }

}
