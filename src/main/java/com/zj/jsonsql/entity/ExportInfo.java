package com.zj.jsonsql.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 导出配置信息
 *
 * @author 19242
 */
@Data
@NoArgsConstructor
public class ExportInfo {
    /**
     * 导出行：0-SQL查询，1-勾选行，2-全部
     */
    private int row;
    /**
     * 导出列：0-SQL查询，1-全部
     */
    private int column;
    /**
     * true 平铺数组 [{"a":"1"},{"a":"2"}] -> ["1","2"]
     */
    private boolean round;
    /**
     * true 美化json
     */
    private boolean beautify = true;
    /**
     * true 去重
     */
    private boolean distinct;
    /**
     * 只有一个元素时，去除数组 [{"a":"1"}] -> {"a":"1"}
     */
    private boolean onlyOne = true;
    /**
     * 展示的json字符串
     */
    private String jsonArrayStr = "";

    public ExportInfo(ExportInfo exportInfo) {
        if (Objects.isNull(exportInfo)) {
            return;
        }
        row = exportInfo.row;
        column = exportInfo.column;
        round = exportInfo.round;
        beautify = exportInfo.beautify;
        distinct = exportInfo.distinct;
        onlyOne = exportInfo.onlyOne;
        jsonArrayStr = exportInfo.jsonArrayStr;
    }
}
