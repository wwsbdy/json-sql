package com.zj.demoplugin.entity;

import com.intellij.util.ui.ColumnInfo;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;

/**
 * @author arthur_zhou
 */
@EqualsAndHashCode(callSuper = true)
public class IdColumnInfo extends ColumnInfo<MyJson, Integer> {

    public IdColumnInfo() {
        super("序号");
    }

    @Nullable
    @Override
    public Integer valueOf(MyJson myJson) {
        return myJson.getId();
    }
}
