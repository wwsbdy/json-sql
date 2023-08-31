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
import com.intellij.openapi.project.Project;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.EditableModel;
import com.zj.demoplugin.entity.json.JsonTable;
import com.zj.demoplugin.utils.MyExecutorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Vector;

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

        JsonTable myTable = new JsonTable(columns, objects);
        JComponent jComponent = ets();
        final Content content = layoutUi.createContent("contentId", jComponent, "displayName2", AllIcons.Debugger.Console, jComponent);
        content.setCloseable(false);
        layoutUi.addContent(content);

        RunContentManager.getInstance(project).showRunContent(executor, descriptor);
    }

    private JBPanel ets() {
        // 数据面板（中间面板）
        JBPanel tableBoxPanel = new JBPanel();
        tableBoxPanel.setLayout(new BorderLayout(0, 0));

        // 表头（列名）
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("编号");
        columnNames.add("姓名");
        columnNames.add("性别");
        columnNames.add("创建时间");
        // 创建表格模型
        DefaultTableModel dataModel = new DefaultTableModel(columnNames, 0);
        // 创建JTable表格组件
        JBTable table = new JBTable(dataModel);
        // 固定表头不可移动
        table.getTableHeader().setReorderingAllowed(false);
        // 创建带滚动条的面板，并将表格添加到带滚动条的面板中
        JBScrollPane scorllPane = new JBScrollPane(table);
        // 将表头添加到面板中（布局的上方）
        tableBoxPanel.add(table.getTableHeader(), BorderLayout.NORTH);
        // 将带滚动条的面板添加到布局中（布局的中间）
        tableBoxPanel.add(scorllPane, BorderLayout.CENTER);
        // 数据列表
        Vector<Vector<Object>> rowData = new Vector<>();
        for (int i = 1; i <= 20; i++) {
            Vector<Object> rowItem = new Vector<>();
            rowItem.add(i);
            rowItem.add("用户" + i);
            rowItem.add(i % 2 == 1 ? "男" : "女");
            rowItem.add(new Date());
            rowData.add(rowItem);
        }
        // 绑定结果
        dataModel.setDataVector(rowData, columnNames);

        JBPanel buttonPanel = new JBPanel();
        JButton button = new JButton("s");
        button.addActionListener(e -> {
            System.out.println(111);
        });
        button.setPreferredSize(new Dimension(25, 25));
        buttonPanel.add(button);
        buttonPanel.setPreferredSize(new Dimension(30, 300));

        return tableBoxPanel;
    }


}