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
import com.intellij.ui.content.Content;
import com.zj.demoplugin.entity.json.JsonTable;
import com.zj.demoplugin.utils.MyExecutorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
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

        JsonTable myTable = new JsonTable(columns, objects);

        final Content content = layoutUi.createContent("contentId", myTable.getBrowsersTable(), "displayName2", AllIcons.Debugger.Console, myTable.getBrowsersTable());
        content.setCloseable(false);
        layoutUi.addContent(content);

        RunContentManager.getInstance(project).showRunContent(executor, descriptor);
    }


}