package com.zj.jsonsql.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.zj.jsonsql.constant.Constant;
import com.zj.jsonsql.entity.IdeaJsonInfo;
import com.zj.jsonsql.enums.NoticeEnum;
import com.zj.jsonsql.utils.ExecutorUtil;
import org.jetbrains.annotations.NotNull;

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
        ExecutorUtil.addContent(new IdeaJsonInfo(), project, toolWindow);
    }

    private void setupAddTabAction(Project project, ToolWindow toolWindow) {
        // 创建"添加Tab"的动作
        AnAction addTabAction = new ToggleAction(PluginBundle.get("tool-window.add-tab"), "", AllIcons.General.Add) {
            @Override
            public @NotNull ActionUpdateThread getActionUpdateThread() {
                return ActionUpdateThread.EDT;
            }

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
