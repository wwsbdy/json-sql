package com.zj.demoplugin.entity;

import com.intellij.util.ui.ColumnInfo;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

/**
 * @author arthur_zhou
 */
@EqualsAndHashCode(callSuper = true)
public class StrColumnInfo extends ColumnInfo<MyJson, String> {
    /**
     * 原始名称
     */
    private final String originalName;

    private final String text;

    public StrColumnInfo(String originalName, @Nls(capitalization = Nls.Capitalization.Title) String name, String text) {
        super(name);
        this.originalName = originalName;
        this.text = text;
    }

    @Nullable
    @Override
    public String valueOf(MyJson myJson) {
        return myJson == null ? "NULL" : getValue(myJson.get(originalName));
    }

    /**
     * 处理value
     *
     * @param value
     * @return
     */
    private String getValue(Object value) {
        if (value == null) {
            return "NULL";
        }
        return value.toString();
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
