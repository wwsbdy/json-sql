package com.zj.jsonsql.ui;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.zj.jsonsql.constant.Constant;
import com.zj.jsonsql.entity.Field;
import com.zj.jsonsql.entity.IdeaJsonInfo;
import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.JsonEnum;
import com.zj.jsonsql.enums.NoticeEnum;
import com.zj.jsonsql.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import javax.swing.*;
import java.util.*;

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
     * @param jsonArray jsonArray
     * @param jsonStr   jsonStr
     */
    protected void end(JSONArray jsonArray, String jsonStr) {
        if (jsonArray.size() > Constant.ROWS_MAX) {
            Messages.showErrorDialog(project, NoticeEnum.ROWS_TOO_MANY.getMessage(), NoticeEnum.ROWS_TOO_MANY.getWarn());
            return;
        }
        List<Row> rowList = new ArrayList<>();
        Map<String, List<JsonEnum>> columnMap = new LinkedHashMap<>();
        String onlyFiled = null;
        for (Object o : jsonArray) {
            Row row;
            if (Objects.isNull(o) || !(o instanceof JSONObject)) {
                // 不是JSONObject, 新定义一个JSONObject放入
                if (Objects.isNull(onlyFiled)) {
                    onlyFiled = Constant.ONLY_FILED + System.currentTimeMillis() / 1000L;
                }
                row = new Row(new JSONObject().fluentPut(onlyFiled, o), rowList);
            } else {
                row = new Row((JSONObject) o, rowList);
            }
            for (String key : row.keySet()) {
                // 可能会出现不同数据里同一个key，value不一样的情况。如：null和string。这时以不是null的为准，其他的情况以最后一个value类型为准
                JsonEnum type = JsonUtil.getType(row.get(key));
                if (columnMap.containsKey(key)) {
                    columnMap.get(key).add(type);
                } else {
                    columnMap.put(key, Lists.newArrayList(type));
                }
            }
            rowList.add(row);
        }
        if (MapUtils.isEmpty(columnMap) || CollectionUtils.isEmpty(rowList)) {
            Messages.showErrorDialog(project, NoticeEnum.JSON_EMPTY.getMessage(), NoticeEnum.JSON_EMPTY.getWarn());
            return;
        }
        if (columnMap.size() > Constant.COLUMNS_MAX) {
            Messages.showErrorDialog(project, NoticeEnum.COLUMNS_TOO_MANY.getMessage(), NoticeEnum.COLUMNS_TOO_MANY.getWarn());
            return;
        }
        ideaJsonInfo.setColumns(Field.getOriginalField(columnMap));
        ideaJsonInfo.setList(rowList);
        ideaJsonInfo.setJsonContent(jsonStr);
        ideaJsonInfo.resetSql();
        // 关闭窗口
        dialog.doCancelAction();
    }
}
