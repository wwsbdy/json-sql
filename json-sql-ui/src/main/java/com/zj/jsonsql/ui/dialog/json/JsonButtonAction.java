package com.zj.jsonsql.ui.dialog.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.zj.jsonsql.entity.IdeaJsonInfo;
import com.zj.jsonsql.enums.NoticeEnum;
import com.zj.jsonsql.ui.ButtonAction;

import java.awt.event.ActionEvent;
import java.util.Objects;

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
        JSONArray jsonArray = null;
        try {
            Object parse = JSON.parse(jsonStr, Feature.OrderedField);
            if (parse instanceof JSONObject) {
                jsonArray = new JSONArray();
                jsonArray.add(parse);
            } else if (parse instanceof JSONArray) {
                jsonArray = JSONArray.parseArray(jsonStr, Feature.OrderedField);
            }
        } catch (Exception exception) {
            Messages.showErrorDialog(project, NoticeEnum.JSON_ERROR.getMessage(), NoticeEnum.JSON_ERROR.getWarn());
            return;
        }
        if (Objects.isNull(jsonArray)) {
            Messages.showErrorDialog(project, NoticeEnum.JSON_ERROR.getMessage(), NoticeEnum.JSON_ERROR.getWarn());
            return;
        }
        end(jsonStr);
    }

}
