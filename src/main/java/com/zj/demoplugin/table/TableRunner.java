package com.zj.demoplugin.table;

import com.alibaba.fastjson.JSONObject;
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
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import com.zj.demoplugin.entity.StrColumnInfo;
import com.zj.demoplugin.form.sql.SqlDialog;
import com.zj.demoplugin.utils.MyExecutorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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


    public void run(Set<String> columns, List<JSONObject> objects) {

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

        JComponent jComponent = getTablePanel(columns, objects);
        final Content content = layoutUi.createContent("contentId", jComponent, "displayName2", AllIcons.Debugger.Console, jComponent);
        content.setCloseable(false);
        layoutUi.addContent(content);

        RunContentManager.getInstance(project).showRunContent(executor, descriptor);
    }

    private JPanel getTablePanel(Set<String> columns, List<JSONObject> objects) {
        ColumnInfo[] columnInfos = new ColumnInfo[columns.size()];
        Object[] array = columns.toArray();
        for (int i = 0; i < array.length; i++) {
            columnInfos[i] = new StrColumnInfo(String.valueOf(array[i]));
        }
        // 创建表格模型
        ListTableModel<JSONObject> dataModel = new ListTableModel<>(columnInfos);
        // 创建JTable表格组件
        TableView<JSONObject> table = new TableView<>(dataModel);
        // 固定表头不可移动
        table.getTableHeader().setReorderingAllowed(false);
        // 绑定结果
        dataModel.setItems(objects);
        // 创建装饰器实例
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(table, null);
        // 禁用新增删除移动按钮
        decorator.disableAddAction();
        decorator.disableDownAction();
        decorator.disableRemoveAction();
        decorator.disableUpAction();
        decorator.disableUpDownActions();

        AnActionButton edit = new AnActionButton("1233ewqd", AllIcons.Actions.Find) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                SqlDialog formTestDialog = new SqlDialog(Objects.requireNonNull(project));
                formTestDialog.show();
            }
        };
        decorator.addExtraAction(edit);
        return decorator.createPanel();
    }


}