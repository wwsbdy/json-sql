package com.zj.demoplugin.ui.dialog.json;

import com.alibaba.fastjson.JSONArray;
import com.intellij.json.JsonLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;
import com.zj.demoplugin.constant.Constant;
import com.zj.demoplugin.entity.Field;
import com.zj.demoplugin.entity.JsonInfo;
import com.zj.demoplugin.entity.MyJson;
import com.zj.demoplugin.enums.NoticeEnum;
import com.zj.demoplugin.ui.edit.CustomEditorField;
import com.zj.demoplugin.ui.table.TableRunner;
import com.zj.demoplugin.utils.JsonUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.*;

/**
 * @author arthur_zhou
 */

public class FormDialog extends DialogWrapper {

    private Project project;

    /**
     * swing样式类，定义在4.3.2
     */
    private final CustomEditorField jsonContent;

    public FormDialog(Project project) {
        super(true);
        // 设置会话框标题
        setTitle("输入json");
        // 获取到当前项目的名称
        this.project = project;
        jsonContent = new CustomEditorField(JsonLanguage.INSTANCE, project, "");
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
                try {
                    JSONArray jsonArray = JSONArray.parseArray(jsonStr);
                    if (Objects.isNull(jsonArray)) {
                        jsonArray = new JSONArray();
                    }
                    if (jsonArray.size() > Constant.ROWS_MAX) {
                        Messages.showErrorDialog(project, NoticeEnum.ROWS_TOO_MANY.getMessage(), NoticeEnum.ROWS_TOO_MANY.getWarn());
                    } else {
                        List<MyJson> objects = new ArrayList<>();
                        Set<Field> columns = new LinkedHashSet<>();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            MyJson object = new MyJson(jsonArray.getJSONObject(i));
                            for (String key : object.keySet()) {
                                columns.add(new Field(key, key, JsonUtil.getType(object.get(key)).name().toLowerCase()));
                            }
                            objects.add(object);
                        }
                        if (columns.size() > Constant.COLUMNS_MAX) {
                            Messages.showErrorDialog(project, NoticeEnum.COLUMNS_TOO_MANY.getMessage(), NoticeEnum.COLUMNS_TOO_MANY.getWarn());
                        } else {
                            JsonInfo jsonInfo = new JsonInfo(new ArrayList<>(columns), objects);
                            // 打开表格
                            new TableRunner(project).run(jsonInfo);
                            // 关闭窗口
                            doCancelAction();
                        }
                    }
                } catch (Exception e) {
                    Messages.showErrorDialog(project, NoticeEnum.JSON_ERROR.getMessage(), NoticeEnum.JSON_ERROR.getWarn());
                }

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

