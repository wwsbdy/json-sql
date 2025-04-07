package com.zj.jsonsql.ui.dialog.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.collect.Lists;
import com.intellij.openapi.ui.Messages;
import com.zj.jsonsql.constant.Constant;
import com.zj.jsonsql.entity.Field;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.JsonEnum;
import com.zj.jsonsql.enums.NoticeEnum;
import com.zj.jsonsql.ui.table.TableRunner;
import com.zj.jsonsql.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;

/**
 * 点击输入json确定按钮事件
 *
 * @author arthur_zhou
 * @date 2023/12/20 15:08
 */
public class ButtonAction extends AbstractAction {

    private final FormDialog formDialog;

    public ButtonAction(FormDialog formDialog) {
        this.formDialog = formDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //获取到name和age
        String jsonStr = formDialog.getJsonContent().getText();
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
            Messages.showErrorDialog(formDialog.getProject(), NoticeEnum.JSON_ERROR.getMessage(), NoticeEnum.JSON_ERROR.getWarn());
            return;
        }
        if (Objects.isNull(jsonArray)) {
            Messages.showErrorDialog(formDialog.getProject(), NoticeEnum.JSON_ERROR.getMessage(), NoticeEnum.JSON_ERROR.getWarn());
            return;
        }
        if (jsonArray.size() > Constant.ROWS_MAX) {
            Messages.showErrorDialog(formDialog.getProject(), NoticeEnum.ROWS_TOO_MANY.getMessage(), NoticeEnum.ROWS_TOO_MANY.getWarn());
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
            Messages.showErrorDialog(formDialog.getProject(), NoticeEnum.JSON_EMPTY.getMessage(), NoticeEnum.JSON_EMPTY.getWarn());
            return;
        }
        if (columnMap.size() > Constant.COLUMNS_MAX) {
            Messages.showErrorDialog(formDialog.getProject(), NoticeEnum.COLUMNS_TOO_MANY.getMessage(), NoticeEnum.COLUMNS_TOO_MANY.getWarn());
            return;
        }
        JsonInfo jsonInfo = new JsonInfo(Field.getOriginalField(columnMap), rowList, jsonStr);
        // 打开表格
        new TableRunner(formDialog.getProject()).run(jsonInfo);
        // 关闭窗口
        formDialog.doCancelAction();
    }
}
