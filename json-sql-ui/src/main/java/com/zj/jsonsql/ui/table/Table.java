package com.zj.jsonsql.ui.table;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.TableView;
import com.intellij.ui.wizard.WizardModel;
import com.intellij.util.ui.ListTableModel;
import com.zj.jsonsql.constant.Constant;
import com.zj.jsonsql.entity.ExportInfo;
import com.zj.jsonsql.entity.IdeaJsonInfo;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.NoticeEnum;
import com.zj.jsonsql.exception.JsonException;
import com.zj.jsonsql.ui.AnActionButtonImpl;
import com.zj.jsonsql.ui.PluginBundle;
import com.zj.jsonsql.ui.dialog.export.Export;
import com.zj.jsonsql.ui.dialog.export.ExportDialog;
import com.zj.jsonsql.ui.dialog.export.Json;
import com.zj.jsonsql.ui.dialog.export.Setting;
import com.zj.jsonsql.ui.dialog.importexcel.ImportDialog;
import com.zj.jsonsql.ui.dialog.json.JsonDialog;
import com.zj.jsonsql.ui.dialog.sql.SqlDialog;
import com.zj.jsonsql.utils.ExecutorUtil;
import com.zj.jsonsql.utils.JsonUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.Objects;

/**
 * 表格
 *
 * @author 19242
 */
@Getter
public class Table implements Disposable {

    private JPanel panel;

    private Table() {
    }

    /**
     * 插件表格
     *
     * @param ideaJsonInfo json信息
     * @return 面板
     */
    @Deprecated
    public static Table create(@NotNull Project project, @NotNull IdeaJsonInfo ideaJsonInfo) {
        return create(project, ideaJsonInfo, null);
    }

    private static void refresh(ListTableModel<Row> dataModel, @NotNull IdeaJsonInfo ideaJsonInfo, TableView<Row> table) {
        // 重新赋值
        dataModel.setColumnInfos(ideaJsonInfo.getFields());
        // 设置序号和选择表头不可改变大小
        setIdAndSelectHeader(table);
        dataModel.setItems(ideaJsonInfo.getResult());
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

    public static Table create(Project project, IdeaJsonInfo ideaJsonInfo, ToolWindow toolWindow) {
        Table myTable = new Table();
        // 创建表格模型
        ListTableModel<Row> dataModel = new ListTableModel<>(ideaJsonInfo.getFields());
        // 创建JTable表格组件
        TableView<Row> table = new TableView<>(dataModel);
        table.setColumnSelectionAllowed(true);
        // 固定表头不可移动
        table.getTableHeader().setReorderingAllowed(false);
        // 设置序号和选择表头不可改变大小
        setIdAndSelectHeader(table);
        // 绑定结果
        dataModel.addRows(ideaJsonInfo.getResult());
        // 创建装饰器实例
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(table, null);
        // 禁用自带的按钮
        disableButton(decorator);
        // 编辑sql按钮
        AnActionButtonImpl modifyJson = new AnActionButtonImpl(PluginBundle.get("table.edit-json"), AllIcons.Actions.Edit) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                JsonDialog jsonDialog = new JsonDialog(project, ideaJsonInfo);
                Disposer.register(this, jsonDialog.getDisposable());
                jsonDialog.show();
                refresh(dataModel, ideaJsonInfo, table);
            }
        };
        // 编辑sql按钮
        AnActionButtonImpl editSql = new AnActionButtonImpl(PluginBundle.get("table.sql-query"), AllIcons.Actions.Find) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                SqlDialog formTestDialog = new SqlDialog(ideaJsonInfo, project);
                Disposer.register(this, formTestDialog.getDisposable());
                formTestDialog.show();
                refresh(dataModel, ideaJsonInfo, table);
            }
        };
        // 重置按钮
        AnActionButtonImpl reset = new AnActionButtonImpl(PluginBundle.get("table.reset"), AllIcons.General.Reset) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                ideaJsonInfo.resetSql();
                refresh(dataModel, ideaJsonInfo, table);
            }
        };
        // 导出按钮
        AnActionButtonImpl export = new AnActionButtonImpl(PluginBundle.get("table.export"), AllIcons.ToolbarDecorator.Export) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                WizardModel wizardModel = new WizardModel(PluginBundle.get("table.export"));
                ExportInfo exportInfo = new ExportInfo();
                Setting setting = new Setting(exportInfo);
                wizardModel.add(setting);
                Json json = new Json(exportInfo, project);
                wizardModel.add(json);
                Export export1 = new Export(exportInfo, project, ideaJsonInfo);
                wizardModel.add(export1);
                ExportDialog wizardDialog = new ExportDialog(exportInfo, ideaJsonInfo, project, true, wizardModel);
                Disposer.register(wizardDialog.getDisposable(), setting);
                Disposer.register(wizardDialog.getDisposable(), json);
                Disposer.register(wizardDialog.getDisposable(), export1);
                Disposer.register(this, wizardDialog.getDisposable());
                wizardDialog.show();
            }
        };
        // 导出按钮
        AnActionButtonImpl importExcel = new AnActionButtonImpl(PluginBundle.get("table.import"), AllIcons.ToolbarDecorator.Import) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                ImportDialog importDialog = new ImportDialog(project, ideaJsonInfo);
                Disposer.register(this, importDialog.getDisposable());
                importDialog.show();
                refresh(dataModel, ideaJsonInfo, table);
            }
        };
        decorator.addExtraActions(modifyJson, editSql, reset, export, importExcel);
        Disposer.register(myTable, modifyJson);
        Disposer.register(myTable, editSql);
        Disposer.register(myTable, reset);
        Disposer.register(myTable, export);
        Disposer.register(myTable, importExcel);
        if (Objects.nonNull(toolWindow)) {
            // 复制
            AnActionButtonImpl copy = new AnActionButtonImpl(PluginBundle.get("table.copy"), AllIcons.Actions.Copy) {
                @Override
                public void actionPerformed(@NotNull AnActionEvent e) {
                    if (toolWindow.getContentManager().getContentCount() >= Constant.TAB_MAX) {
                        Messages.showErrorDialog(project, NoticeEnum.TAB_TOO_MANY.getMessage(), NoticeEnum.TAB_TOO_MANY.getWarn());
                        return;
                    }
                    IdeaJsonInfo nextIdeaJsonInfo = new IdeaJsonInfo();
                    String jsonStr = JsonUtil.getJsonStr(ideaJsonInfo, null);
                    nextIdeaJsonInfo.setJsonContent(jsonStr);
                    try {
                        JsonInfo jsonInfo = JsonUtil.getJsonInfo(jsonStr);
                        nextIdeaJsonInfo.setColumns(jsonInfo.getColumns());
                        nextIdeaJsonInfo.setList(jsonInfo.getList());
                    } catch (JsonException ignored) {
                    }
                    ExecutorUtil.addContent(nextIdeaJsonInfo, project, toolWindow);
                }
            };
            decorator.addExtraAction(copy);
            Disposer.register(myTable, copy);
        }
        myTable.panel = decorator.createPanel();
        return myTable;
    }


    @Override
    public void dispose() {

    }
}
