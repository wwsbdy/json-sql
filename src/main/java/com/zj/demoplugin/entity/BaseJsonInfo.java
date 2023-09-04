package com.zj.demoplugin.entity;

import com.alibaba.fastjson.JSONObject;
import com.intellij.util.ui.ColumnInfo;

import java.util.List;

/**
 * @author arthur_zhou
 */
public abstract class BaseJsonInfo {
    /**
     * 原始列
     */
    private List<String> columns;
    /**
     * 导入数据列表
     */
    private List<JSONObject> list;

    public List<String> getColumns() {
        return columns;
    }

    public List<JSONObject> getList() {
        return list;
    }

    public BaseJsonInfo(List<String> columns, List<JSONObject> list) {
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
    public abstract List<JSONObject> getRows();
}
