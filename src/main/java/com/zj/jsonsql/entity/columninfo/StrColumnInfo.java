package com.zj.jsonsql.entity.columninfo;

import com.intellij.ui.ColoredTableCellRenderer;
import com.intellij.ui.JBColor;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ui.ColumnInfo;
import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.JsonEnum;
import lombok.EqualsAndHashCode;
import org.apache.calcite.sql.SqlNode;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Objects;

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
    private final SqlNode originalFiled;

    private final String text;

    public StrColumnInfo(SqlNode originalFiled, @Nls(capitalization = Nls.Capitalization.Title) String name, JsonEnum type) {
        super(name);
        this.originalFiled = originalFiled;
        this.text = type.name().toLowerCase();
    }

    @Nullable
    @Override
    public String valueOf(Row row) {
        Object value = row.get(originalFiled);
        return Objects.isNull(value) ? "NULL" : value.toString();
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


    /**
     * 单元格字体颜色
     *
     * @param myRow 当前行
     * @return TableCellRenderer
     */
    @Override
    public @Nullable TableCellRenderer getRenderer(Row myRow) {
        return new ColoredTableCellRenderer() {
            @Override
            protected void customizeCellRenderer(JTable table, Object value, boolean selected, boolean hasFocus, int row, int column) {
                // NULL的颜色
                if (Objects.isNull(myRow.get(originalFiled))) {
                    append((String) value, new SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, new JBColor(new Color(255, 153, 0, 168), new Color(255, 153, 0, 168))));
                } else {
                    append((String) value, SimpleTextAttributes.REGULAR_ATTRIBUTES);
                }
            }
        };
    }
}
