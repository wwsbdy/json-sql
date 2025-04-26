package com.zj.jsonsql.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.zj.jsonsql.entity.IdeaJsonInfo;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.exception.JsonException;
import com.zj.jsonsql.utils.JsonUtil;

import javax.swing.*;

/**
 * json确定按钮事件
 *
 * @author arthur_zhou
 * @date 2023/12/20 15:08
 */
public abstract class ButtonAction<Dialog extends DialogWrapper> extends AbstractAction {

    protected final Project project;
    protected final Dialog dialog;
    protected final IdeaJsonInfo ideaJsonInfo;

    public ButtonAction(Project project, Dialog dialog, IdeaJsonInfo ideaJsonInfo) {
        this.project = project;
        this.dialog = dialog;
        this.ideaJsonInfo = ideaJsonInfo;
    }


    /**
     * 按钮事件结束调用，设置jsonInfo的值
     *
     * @param jsonStr   jsonStr
     */
    protected void end(String jsonStr) {
        JsonInfo jsonInfo;
        try {
            jsonInfo = JsonUtil.getJsonInfo(jsonStr);
        } catch (JsonException e) {
            Messages.showErrorDialog(project, e.getMessage(), e.getNoticeEnum().getWarn());
            return;
        }
        ideaJsonInfo.setColumns(jsonInfo.getColumns());
        ideaJsonInfo.setList(jsonInfo.getList());
        ideaJsonInfo.setJsonContent(jsonStr);
        ideaJsonInfo.resetSql();
        // 关闭窗口
        dialog.doCancelAction();
    }
}
