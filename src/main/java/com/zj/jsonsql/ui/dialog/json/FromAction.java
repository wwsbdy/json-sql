package com.zj.jsonsql.ui.dialog.json;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.Objects;

/**
 * @author arthur_zhou
 */

public class FromAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        FormDialog formTestDialog = new FormDialog(Objects.requireNonNull(e.getProject()));
        //是否允许用户通过拖拽的方式扩大或缩小你的表单框，我这里定义为true，表示允许
        formTestDialog.setResizable(true);
        formTestDialog.show();
    }
}

