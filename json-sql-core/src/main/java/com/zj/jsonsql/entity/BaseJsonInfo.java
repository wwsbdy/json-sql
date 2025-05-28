package com.zj.jsonsql.entity;

import lombok.Data;

import java.util.List;

/**
 * 原始表格数据
 *
 * @author arthur_zhou
 */
@Data
public abstract class BaseJsonInfo {
    /**
     * 传入的原始json字符串
     */
    private String jsonContent;
    /**
     * 原始列
     */
    private List<Field> columns;
    /**
     * 导入数据列表
     */
    private List<Row> list;

    public BaseJsonInfo(List<Field> columns, List<Row> list, String jsonContent) {
        this.columns = columns;
        this.list = list;
        this.jsonContent = jsonContent;
    }
}
