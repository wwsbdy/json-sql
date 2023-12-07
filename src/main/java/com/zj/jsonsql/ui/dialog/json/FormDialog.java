package com.zj.jsonsql.ui.dialog.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.collect.Lists;
import com.intellij.json.JsonLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;
import com.zj.jsonsql.constant.Constant;
import com.zj.jsonsql.entity.Field;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.JsonEnum;
import com.zj.jsonsql.enums.NoticeEnum;
import com.zj.jsonsql.ui.edit.CustomEditorField;
import com.zj.jsonsql.ui.table.TableRunner;
import com.zj.jsonsql.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.*;

/**
 * @author arthur_zhou
 */
public class FormDialog extends DialogWrapper {

    private final Project project;

    /**
     * swing样式类，定义在4.3.2
     */
    private final CustomEditorField jsonContent;

    public FormDialog(Project project, String content) {
        super(true);
        if (Objects.isNull(content)) {
            content = "";
        }
        // 设置会话框标题
        setTitle("输入json");
        // 获取到当前项目的名称
        this.project = project;
        jsonContent = new CustomEditorField(JsonLanguage.INSTANCE, project, content);
        jsonContent.setPreferredSize(new Dimension(500, 700));
        // 触发一下init方法，否则swing样式将无法展示在会话框
        init();
    }

    @Override
    protected JComponent createNorthPanel() {
//        final JPanel north = new JPanel();
//        // 定义表单的标题部分，放置到IDEA会话框的顶部位置
//        JLabel title = new JLabel("表单标题");
//        // 字体样式
//        title.setFont(new Font("微软雅黑", Font.PLAIN, 26));
//        // 水平居中
//        title.setHorizontalAlignment(SwingConstants.CENTER);
//        // 垂直居中
//        title.setVerticalAlignment(SwingConstants.CENTER);
//        north.add(title);
//
//        return north;
        return null;
    }

    @Override
    protected JComponent createSouthPanel() {
        final JPanel south = new JPanel();
        //定义表单的提交按钮，放置到IDEA会话框的底部位置
        JButton submit = new JButton("提交");
        // 水平居中
        submit.setHorizontalAlignment(SwingConstants.CENTER);
        // 垂直居中
        submit.setVerticalAlignment(SwingConstants.CENTER);
        south.add(submit);
        //按钮事件绑定
        submit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //获取到name和age
                String jsonStr = jsonContent.getText();
                JSONArray jsonArray = null;
                try {
                    Object parse = JSON.parse(jsonStr, Feature.OrderedField);
                    if (parse instanceof JSONObject) {
                        jsonArray = new JSONArray();
                        jsonArray.add(parse);
                    } else if (parse instanceof JSONArray) {
                        jsonArray = JSONArray.parseArray(jsonStr, Feature.OrderedField);
                    }
                } catch (Exception e) {
                    Messages.showErrorDialog(project, NoticeEnum.JSON_ERROR.getMessage(), NoticeEnum.JSON_ERROR.getWarn());
                    return;
                }
                if (Objects.isNull(jsonArray)) {
                    Messages.showErrorDialog(project, NoticeEnum.JSON_ERROR.getMessage(), NoticeEnum.JSON_ERROR.getWarn());
                    return;
                }
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
                        row = new Row(new JSONObject().fluentPut(onlyFiled, o));
                    } else {
                        row = new Row((JSONObject) o);
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
                JsonInfo jsonInfo = new JsonInfo(Field.getOriginalField(columnMap), rowList, jsonStr);
                // 打开表格
                new TableRunner(project).run(jsonInfo);
                // 关闭窗口
                doCancelAction();
            }
        });
        return south;
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(1, 1, JBUI.emptyInsets(), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel center = new JPanel();
        center.setLayout(new BorderLayout(0, 0));
        center.setPreferredSize(new Dimension(750, 300));
        panel1.add(center, BorderLayout.CENTER);
        center.add(jsonContent, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panel1.add(panel2, BorderLayout.SOUTH);
        return contentPanel;
    }
}

