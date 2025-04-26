package com.zj.jsonsql.ui.dialog.json;

import com.intellij.openapi.project.Project;
import com.zj.jsonsql.entity.IdeaJsonInfo;
import com.zj.jsonsql.ui.ButtonAction;

import java.awt.event.ActionEvent;

/**
 * @author : jie.zhou
 * @date : 2025/4/18
 */
public class JsonButtonAction extends ButtonAction<JsonDialog> {
    public JsonButtonAction(Project project, JsonDialog dialog, IdeaJsonInfo jsonInfo) {
        super(project, dialog, jsonInfo);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String jsonStr = dialog.getJsonContent().getText();
        end(jsonStr);
    }

}
