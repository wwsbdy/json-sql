package com.zj.demoplugin.entity;

import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.ColumnInfo;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

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

    @Override
    public int getWidth(JTable table) {
        return 40;
    }

    @Override
    public @Nullable TableCellRenderer getRenderer(MyJson myJson) {
        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
        tableCellRenderer.setHorizontalAlignment(JBLabel.CENTER);
        return tableCellRenderer;
    }
}
