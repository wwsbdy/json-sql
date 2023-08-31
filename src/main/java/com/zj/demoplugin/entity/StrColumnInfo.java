package com.zj.demoplugin.entity;

import com.alibaba.fastjson.JSONObject;
import com.intellij.util.ui.ColumnInfo;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

/**
 * @author arthur_zhou
 */
@EqualsAndHashCode(callSuper = true)
public class StrColumnInfo extends ColumnInfo<JSONObject, String> {

    public StrColumnInfo(@Nls(capitalization = Nls.Capitalization.Title) String name) {
        super(name);
    }

    @Nullable
    @Override
    public String valueOf(JSONObject jsonObject) {
        return jsonObject == null ? "NULL" : getValue(jsonObject.getString(getName()));
    }

    /**
     * 处理value
     *
     * @param value
     * @return
     */
    private String getValue(String value) {
        if (value == null) {
            return "NULL";
        }
        return value;
    }
}
