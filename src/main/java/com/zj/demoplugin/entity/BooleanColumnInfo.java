package com.zj.demoplugin.entity;

import com.intellij.util.ui.ColumnInfo;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

/**
 * @author arthur_zhou
 */
@EqualsAndHashCode(callSuper = true)
public class BooleanColumnInfo extends ColumnInfo<MyJson, Boolean> {

    public BooleanColumnInfo(@Nls(capitalization = Nls.Capitalization.Title) String name) {
        super(name);
    }

    @Nullable
    @Override
    public Boolean valueOf(MyJson myJson) {
        return myJson.isSelected();
    }

    @Override
    public Class<?> getColumnClass() {
        return Boolean.class;
    }

    @Override
    public void setValue(MyJson myJson, Boolean value) {
        myJson.setSelected(value);
    }

    @Override
    public boolean isCellEditable(MyJson myJson) {
        return true;
    }
}
