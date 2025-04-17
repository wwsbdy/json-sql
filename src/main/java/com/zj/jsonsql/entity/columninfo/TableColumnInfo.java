package com.zj.jsonsql.entity.columninfo;

import com.intellij.util.ui.ColumnInfo;
import com.zj.jsonsql.entity.Row;

public abstract class TableColumnInfo<T> extends ColumnInfo<Row, T> {

    public TableColumnInfo(String name) {
        super(name);
    }
}
