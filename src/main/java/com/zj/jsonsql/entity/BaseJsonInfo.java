package com.zj.jsonsql.entity;

import com.intellij.util.ui.ColumnInfo;

import java.util.List;

/**
 * @author arthur_zhou
 */
public abstract class BaseJsonInfo {
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

    public BaseJsonInfo(List<Field> columns, List<MyJson> list) {
        this.columns = columns;
        this.list = list;
    }

    /**
     * 获取字段数组
     *
     * @return
     */
    public abstract ColumnInfo<?, ?>[] getFields();

    /**
     * 获取行数据
     *
     * @return
     */
    public abstract List<MyJson> getRows();
}
