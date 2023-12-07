package com.zj.jsonsql.entity.columninfo;

import com.intellij.util.ui.ColumnInfo;
import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.JsonEnum;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

/**
 * 普通列展示配置
 *
 * @author arthur_zhou
 */
@EqualsAndHashCode(callSuper = true)
public class StrColumnInfo extends ColumnInfo<Row, String> {
    /**
     * 原始名称
     */
    private final String originalName;

    private final String text;

    public StrColumnInfo(String originalName, @Nls(capitalization = Nls.Capitalization.Title) String name, JsonEnum type) {
        super(name);
        this.originalName = originalName;
        this.text = type.name().toLowerCase();
    }

    @Nullable
    @Override
    public String valueOf(Row row) {
        return row == null ? "NULL" : getValue(row.get(originalName));
    }

    /**
     * 处理value
     *
     * @param value value
     * @return 处理过的value
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
     * @return 表头的提示信息
     */
    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @Nullable String getTooltipText() {
        return text;
    }
}
