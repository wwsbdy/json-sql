package com.zj.jsonsql.ui.table;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.TableView;
import com.intellij.ui.wizard.WizardModel;
import com.intellij.util.ui.ListTableModel;
import com.zj.jsonsql.entity.ExportInfo;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.ui.dialog.export.Export;
import com.zj.jsonsql.ui.dialog.export.Json;
import com.zj.jsonsql.ui.dialog.export.MyWizardDialog;
import com.zj.jsonsql.ui.dialog.export.Setting;
import com.zj.jsonsql.ui.dialog.json.FormDialog;
import com.zj.jsonsql.ui.dialog.sql.SqlDialog;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.TableColumn;

/**
 * 表格
 *
 * @author 19242
 */
public class Table {

    /**
     * 插件表格
     *
     * @param jsonInfo json信息
     * @return 面板
     */
    public static JPanel create(@NotNull Project project, @NotNull JsonInfo jsonInfo) {
        // 创建表格模型
        ListTableModel<Row> dataModel = new ListTableModel<>(jsonInfo.getFields());
        // 创建JTable表格组件
        TableView<Row> table = new TableView<>(dataModel);
        table.setColumnSelectionAllowed(true);
        // 固定表头不可移动
        table.getTableHeader().setReorderingAllowed(false);
        // 设置序号和选择表头不可改变大小
        setIdAndSelectHeader(table);
        // 绑定结果
        dataModel.addRows(jsonInfo.getRows());
        // 创建装饰器实例
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(table, null);
        // 禁用自带的按钮
        disableButton(decorator);
        // 编辑sql按钮
        AnActionButton modifyJson = new AnActionButton("编辑json", AllIcons.Actions.Edit) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                new FormDialog(project, jsonInfo.getJsonContent()).show();
            }
        };
        // 编辑sql按钮
        AnActionButton editSql = new AnActionButton("SQL查询", AllIcons.Actions.Find) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                SqlDialog formTestDialog = new SqlDialog(jsonInfo);
                formTestDialog.show();
                // 重新赋值
                dataModel.setColumnInfos(jsonInfo.getFields());
                // 设置序号和选择表头不可改变大小
                setIdAndSelectHeader(table);
                dataModel.setItems(jsonInfo.getRows());
            }
        };
        // 重置按钮
        AnActionButton reset = new AnActionButton("重置", AllIcons.General.Reset) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                jsonInfo.resetSql();
                dataModel.setColumnInfos(jsonInfo.getFields());
                dataModel.setItems(jsonInfo.getRows());
                // 设置序号和选择表头不可改变大小
                setIdAndSelectHeader(table);
                dataModel.setItems(jsonInfo.getRows());
            }
        };
        // 导出按钮
        AnActionButton export = new AnActionButton("导出", AllIcons.Actions.Commit) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                WizardModel wizardModel = new WizardModel("导出");
                ExportInfo exportInfo = new ExportInfo();
                wizardModel.add(new Setting(exportInfo));
                wizardModel.add(new Json(exportInfo, project));
                wizardModel.add(new Export(exportInfo, project));
                MyWizardDialog wizardDialog = new MyWizardDialog(exportInfo, jsonInfo, project, true, wizardModel);
                wizardDialog.show();
            }
        };
        decorator.addExtraAction(modifyJson);
        decorator.addExtraAction(editSql);
        decorator.addExtraAction(reset);
        decorator.addExtraAction(export);
        return decorator.createPanel();
    }

    /**
     * 设置序号和选择表头不可改变大小
     *
     * @param table 表格视图
     */
    private static void setIdAndSelectHeader(TableView<Row> table) {
        TableColumn column = table.getTableHeader().getColumnModel().getColumn(0);
        column.setResizable(false);
        column.setMaxWidth(40);
        TableColumn column1 = table.getTableHeader().getColumnModel().getColumn(1);
        column1.setResizable(false);
        column1.setMaxWidth(40);
    }

    /**
     * 禁用自带的按钮
     *
     * @param decorator ToolbarDecorator
     */
    private static void disableButton(ToolbarDecorator decorator) {
        // 禁用新增删除移动按钮
        decorator.disableAddAction();
        decorator.disableDownAction();
        decorator.disableRemoveAction();
        decorator.disableUpAction();
        decorator.disableUpDownActions();
    }

}
