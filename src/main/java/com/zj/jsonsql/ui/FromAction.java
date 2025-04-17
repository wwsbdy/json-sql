package com.zj.jsonsql.ui;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.zj.jsonsql.ui.dialog.json.FormDialog;
import com.zj.jsonsql.ui.table.TableRunner;

import java.util.Objects;

/**
 * @author arthur_zhou
 */
@Deprecated
public class FromAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        FormDialog formDialog = new FormDialog(Objects.requireNonNull(e.getProject()), "");
        formDialog.show();
        // 打开表格
        new TableRunner(formDialog.getProject()).run(formDialog.getJsonInfo());
    }
}

