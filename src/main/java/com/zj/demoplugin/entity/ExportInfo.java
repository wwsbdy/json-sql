package com.zj.demoplugin.entity;

import lombok.Data;

/**
 * @author 19242
 */
@Data
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
     * true 平铺数组
     */
    private boolean round;
    /**
     * true 美化json
     */
    private boolean beautify;
    /**
     * 展示的json字符串
     */
    private String jsonArrayStr = "";

}
