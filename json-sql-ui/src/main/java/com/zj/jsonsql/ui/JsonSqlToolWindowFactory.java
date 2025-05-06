package com.zj.jsonsql.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.zj.jsonsql.constant.Constant;
import com.zj.jsonsql.entity.IdeaJsonInfo;
import com.zj.jsonsql.enums.NoticeEnum;
import com.zj.jsonsql.ui.table.Table;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

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
        if (toolWindow.getContentManager().getContentCount() >= Constant.TAB_MAX) {
            Messages.showErrorDialog(project, NoticeEnum.TAB_TOO_MANY.getMessage(), NoticeEnum.TAB_TOO_MANY.getWarn());
            return;
        }
        JComponent jComponent = Table.create(project, new IdeaJsonInfo(), toolWindow);
        Content content = ContentFactory.SERVICE.getInstance()
                .createContent(jComponent, PluginBundle.get("tool-window.title") + toolWindow.getContentManager().getContentCount(), false);
        content.setCloseable(true);

        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().setSelectedContent(content);
    }

    private void setupAddTabAction(Project project, ToolWindow toolWindow) {
        // 创建"添加Tab"的动作
        AnAction addTabAction = new ToggleAction(PluginBundle.get("tool-window.add-tab"), "", AllIcons.General.Add) {
            @Override
            public boolean isSelected(@NotNull AnActionEvent anActionEvent) {
                return false;
            }

            @Override
            public void setSelected(@NotNull AnActionEvent anActionEvent, boolean b) {
                addContent(project, toolWindow);
            }
        };
        if (toolWindow instanceof ToolWindowEx) {
            ((ToolWindowEx) toolWindow).setTabActions(addTabAction);
        }
    }
}
