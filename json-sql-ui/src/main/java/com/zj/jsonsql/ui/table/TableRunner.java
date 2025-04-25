package com.zj.jsonsql.ui.table;

import com.intellij.execution.DefaultExecutionResult;
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
import com.zj.jsonsql.entity.IdeaJsonInfo;
import com.zj.jsonsql.ui.PluginBundle;
import com.zj.jsonsql.utils.MyExecutorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author arthur_zhou
 */
@Deprecated
public class TableRunner {

    /**
     * Project
     */
    private final Project project;


    public TableRunner(@NotNull Project project) {
        this.project = project;
    }

    public void run(@NotNull IdeaJsonInfo jsonInfo) {
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
            @Override
            public @Nullable RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
                return null;
            }

            @Override
            public @NotNull String getName() {
                return "running";
            }

            @Override
            public @NotNull Icon getIcon() {
                return AllIcons.Actions.Show;
            }
        }, new DefaultExecutionResult(), layoutUi);
        descriptor.setExecutionId(System.nanoTime());

        JComponent jComponent = Table.create(project, jsonInfo);
        final Content content = layoutUi.createContent("contentId", jComponent, PluginBundle.get("table.json-data"), AllIcons.Toolwindows.ToolWindowMessages, jComponent);
        content.setCloseable(false);
        layoutUi.addContent(content);

        RunContentManager.getInstance(project).showRunContent(executor, descriptor);
    }

}