package com.zj.jsonsql.entity.columninfo;

import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.ColumnInfo;
import com.zj.jsonsql.entity.Row;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * 序号列格式信息
 *
 * @author arthur_zhou
 */
@EqualsAndHashCode(callSuper = true)
public class IdColumnInfo extends ColumnInfo<Row, Integer> {

    public IdColumnInfo() {
        super("序号");
    }

    @Nullable
    @Override
    public Integer valueOf(Row row) {
        return row.getId();
    }

    @Override
    public @Nullable TableCellRenderer getRenderer(Row row) {
        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
        tableCellRenderer.setHorizontalAlignment(JBLabel.CENTER);
        return tableCellRenderer;
    }
}
