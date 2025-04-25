package com.zj.jsonsql.entity.columninfo;

import com.zj.jsonsql.entity.Row;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

/**
 * 选择列格式信息
 *
 * @author arthur_zhou
 */
@EqualsAndHashCode(callSuper = true)
public class BooleanColumnInfo extends TableColumnInfo<Boolean> {

    public BooleanColumnInfo(@Nls(capitalization = Nls.Capitalization.Title) String name) {
        super(name);
    }

    @Nullable
    @Override
    public Boolean valueOf(Row row) {
        return row.isSelected();
    }

    @Override
    public Class<?> getColumnClass() {
        return Boolean.class;
    }

    @Override
    public void setValue(Row row, Boolean value) {
        row.setSelected(value);
    }

    @Override
    public boolean isCellEditable(Row row) {
        return true;
    }
}
