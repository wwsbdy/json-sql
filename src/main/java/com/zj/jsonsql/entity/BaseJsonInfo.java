package com.zj.jsonsql.entity;

import com.intellij.util.ui.ColumnInfo;
import lombok.Getter;

import java.util.List;

/**
 * 原始表格数据
 *
 * @author arthur_zhou
 */
@Getter
public abstract class BaseJsonInfo {
    /**
     * 传入的原始json字符串
     */
    private final String jsonContent;
    /**
     * 原始列
     */
    private final List<Field> columns;
    /**
     * 导入数据列表
     */
    private final List<Row> list;

    public BaseJsonInfo(List<Field> columns, List<Row> list, String jsonContent) {
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
    public abstract List<Row> getRows();
}
