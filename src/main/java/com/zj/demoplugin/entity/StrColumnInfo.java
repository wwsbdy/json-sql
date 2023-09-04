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
    /**
     * 原始名称
     */
    private String originalName;

    private String text;

    public StrColumnInfo(String originalName, @Nls(capitalization = Nls.Capitalization.Title) String name) {
        super(name);
        this.originalName = originalName;
    }

    @Nullable
    @Override
    public String valueOf(JSONObject jsonObject) {
        return jsonObject == null ? "NULL" : getValue(jsonObject.getString(originalName));
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

    /**
     * 表头提示
     *
     * @return
     */
    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @Nullable String getTooltipText() {
        return text;
    }
}
