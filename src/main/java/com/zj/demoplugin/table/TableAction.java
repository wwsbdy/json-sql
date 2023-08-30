package com.zj.demoplugin.table;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.zj.demoplugin.utils.MyProjectUtil;
import org.jetbrains.annotations.NotNull;

public class TableAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        runExecutor(e.getProject());
    }

    public void runExecutor(Project project) {
        if (project == null) {
            return;
        }

        MyProjectUtil.setRunning(project, "running", true);

        TableRunner executor = new TableRunner(project);

        executor.run();
    }
}