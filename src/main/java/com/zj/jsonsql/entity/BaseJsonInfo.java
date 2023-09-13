package com.zj.jsonsql.entity;

import com.intellij.util.ui.ColumnInfo;

import java.util.List;

/**
 * @author arthur_zhou
 */
public abstract class BaseJsonInfo {

    private final String jsonContent;

    /**
     * 原始列
     */
    private final List<Field> columns;
    /**
     * 导入数据列表
     */
    private final List<MyJson> list;

    public List<Field> getColumns() {
        return columns;
    }

    public List<MyJson> getList() {
        return list;
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public BaseJsonInfo(List<Field> columns, List<MyJson> list, String jsonContent) {
        this.columns = columns;
        this.list = list;
        this.jsonContent = jsonContent;
    }

    /**
     * 获取字段数组
     *
     * @return 表头
     */
    public abstract ColumnInfo<?, ?>[] getFields();

    /**
     * 获取行数据
     *
     * @return 表数据
     */
    public abstract List<MyJson> getRows();
}
