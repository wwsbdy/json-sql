package com.zj.jsonsql.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.ui.table.Table;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collections;

/**
 * @author : jie.zhou
 * @date : 2025/4/17
 */
public class JsonSqlToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        addContent(project, toolWindow);
        setupAddTabAction(project, toolWindow);
    }

    public void addContent(Project project, ToolWindow toolWindow) {
        JComponent jComponent = Table.create(project, new JsonInfo());
        Content content = ContentFactory.getInstance()
                .createContent(jComponent, PluginBundle.get("tool-window.title") + toolWindow.getContentManager().getContentCount(), false);
        content.setCloseable(true);

        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().setSelectedContent(content);
    }

    private void setupAddTabAction(Project project, ToolWindow toolWindow) {
        // 创建"添加Tab"的动作
        AnAction addTabAction = new AnAction(PluginBundle.get("tool-window.add-tab"), "", AllIcons.General.Add) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                addContent(project, toolWindow);
            }
        };
        // 将动作添加到ToolWindow的标题栏
        toolWindow.setTitleActions(Collections.singletonList(addTabAction));
        if (toolWindow instanceof ToolWindowEx) {
            ((ToolWindowEx) toolWindow).setTabActions(addTabAction);
        }
    }
}
