package com.zj.jsonsql.ui;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.zj.jsonsql.ui.dialog.json.JsonDialog;
import com.zj.jsonsql.ui.table.TableRunner;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author arthur_zhou
 */
@Deprecated
public class FromAction extends AnAction {
    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        JsonDialog jsonDialog = new JsonDialog(Objects.requireNonNull(e.getProject()), "");
        jsonDialog.show();
        // 打开表格
        new TableRunner(jsonDialog.getProject()).run(jsonDialog.getJsonInfo());
    }
}

