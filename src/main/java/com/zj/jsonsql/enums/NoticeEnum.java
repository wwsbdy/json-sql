package com.zj.jsonsql.enums;

import com.zj.jsonsql.ui.PluginBundle;
import lombok.Getter;

/**
 * @author 19242
 */
@Getter
public enum NoticeEnum {
    /**
     * json错误
     */
    JSON_ERROR(PluginBundle.get("error.title.json-error"), PluginBundle.get("error.message.json-error")),
    /**
     * sql错误
     */
    SQL_ERROR(PluginBundle.get("error.title.sql-error"), PluginBundle.get("error.message.sql-error")),
    /**
     * Json过长
     */
    ROWS_TOO_MANY(PluginBundle.get("error.title.rows-too-many"), PluginBundle.get("error.message.rows-too-many")),
    /**
     * Json过长
     */
    COLUMNS_TOO_MANY(PluginBundle.get("error.title.columns-too-many"), PluginBundle.get("error.message.columns-too-many")),
    /**
     * SQL过长
     */
    SQL_TOO_LONG(PluginBundle.get("error.title.sql-too-long"), PluginBundle.get("error.message.sql-too-long")),
    /**
     * 输入json为空
     */
    JSON_EMPTY(PluginBundle.get("error.title.json-empty"), PluginBundle.get("error.message.json-empty")),
    /**
     * 文件夹路径为空
     */
    FOLDER_EMPTY(PluginBundle.get("error.title.folder-empty"), PluginBundle.get("error.message.folder-empty")),
    /**
     * 文件类型错误
     */
    FILE_TYPE_ERROR(PluginBundle.get("error.title.file-type-error"), PluginBundle.get("error.message.file-type-error")),
    /**
     * 文件名错误
     */
    FILE_NAME_ERROR(PluginBundle.get("error.title.file-name-error"), PluginBundle.get("error.message.file-name-error")),
    /**
     * 文件导出失败
     */
    FILE_EXPORT_FAIL(PluginBundle.get("error.title.file-export-fail"), PluginBundle.get("error.message.file-export-fail")),
    /**
     * 文件路径错误
     */
    FILE_PATH_ERROR(PluginBundle.get("error.title.file-path-error"), PluginBundle.get("error.message.file-path-error")),
    /**
     * 文件读取失败
     */
    FILE_READ_ERROR(PluginBundle.get("error.title.file-read-error"), PluginBundle.get("error.message.file-read-error")),
    ;

    private final String warn;
    private final String message;

    NoticeEnum(String warn, String message) {
        this.warn = warn;
        this.message = message;
    }

}
