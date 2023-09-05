package com.zj.demoplugin.table;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunContentManager;
import com.intellij.execution.ui.RunnerLayoutUi;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.content.Content;
import com.intellij.ui.table.TableView;
import com.intellij.util.ui.ListTableModel;
import com.zj.demoplugin.entity.JsonInfo;
import com.zj.demoplugin.entity.MyJson;
import com.zj.demoplugin.form.sql.SqlDialog;
import com.zj.demoplugin.utils.MyExecutorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author arthur_zhou
 */
public class TableRunner {

    /**
     * Project
     */
    private Project project;


    public TableRunner(@NotNull Project project) {
        this.project = project;
    }

    public void run(JsonInfo jsonInfo) {
        // 返回定义的 Executor
        Executor executor = MyExecutorUtil.getRunExecutorInstance(TableExecutor.PLUGIN_ID);
        if (executor == null) {
            return;
        }
        // 创建 RunnerLayoutUi
        final RunnerLayoutUi.Factory factory = RunnerLayoutUi.Factory.getInstance(project);
        RunnerLayoutUi layoutUi = factory.create("id2", "title2", "session name2", project);

        // 创建描述信息
        RunContentDescriptor descriptor = new RunContentDescriptor(new RunProfile() {
            @Nullable
            @Override
            public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
                return null;
            }

            @NotNull
            @Override
            public String getName() {
                return "name";
            }

            @Nullable
            @Override
            public Icon getIcon() {
                return null;
            }
        }, new DefaultExecutionResult(), layoutUi);
        descriptor.setExecutionId(System.nanoTime());

        JComponent jComponent = getTablePanel(jsonInfo);
        final Content content = layoutUi.createContent("contentId", jComponent, "displayName2", AllIcons.Debugger.Console, jComponent);
        content.setCloseable(false);
        layoutUi.addContent(content);

        RunContentManager.getInstance(project).showRunContent(executor, descriptor);
    }

    private JPanel getTablePanel(JsonInfo jsonInfo) {
        // 创建表格模型
        ListTableModel<MyJson> dataModel = new ListTableModel<>(jsonInfo.getFields());
        // 创建JTable表格组件
        TableView<MyJson> table = new TableView<>(dataModel);
        table.setColumnSelectionAllowed(true);
        // 固定表头不可移动
        table.getTableHeader().setReorderingAllowed(false);
        // 绑定结果
        dataModel.addRows(jsonInfo.getRows());
        // 创建装饰器实例
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(table, null);
        // 禁用新增删除移动按钮
        decorator.disableAddAction();
        decorator.disableDownAction();
        decorator.disableRemoveAction();
        decorator.disableUpAction();
        decorator.disableUpDownActions();
        // 编辑sql按钮
        AnActionButton editSql = new AnActionButton("SQL查询", AllIcons.Actions.Find) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                SqlDialog formTestDialog = new SqlDialog(project, jsonInfo);
                formTestDialog.show();
                // 重新赋值
                dataModel.setColumnInfos(jsonInfo.getFields());
                dataModel.setItems(jsonInfo.getRows());
            }
        };
        decorator.addExtraAction(editSql);
        // 重置按钮
        AnActionButton reset = new AnActionButton("重置", AllIcons.General.Reset) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                jsonInfo.resetSql();
                dataModel.setColumnInfos(jsonInfo.getFields());
                dataModel.setItems(jsonInfo.getRows());
            }
        };
        decorator.addExtraAction(reset);
        return decorator.createPanel();
    }


}