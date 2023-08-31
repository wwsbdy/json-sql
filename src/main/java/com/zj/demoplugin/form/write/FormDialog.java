package com.zj.demoplugin.form.write;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.zj.demoplugin.table.TableRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author arthur_zhou
 */

public class FormDialog extends DialogWrapper {

    private Project project;

    /**
     * swing样式类，定义在4.3.2
     */
    private final JLabel json = new JLabel("json：");
    private final JTextArea jsonContent = new JTextArea();

    public FormDialog(Project project) {
        super(true);
        // 设置会话框标题
        setTitle("表单测试~~");
        // 获取到当前项目的名称
        this.project = project;
        // 触发一下init方法，否则swing样式将无法展示在会话框
        init();
    }

    @Override
    protected JComponent createNorthPanel() {
        final JPanel north = new JPanel();
        // 定义表单的标题部分，放置到IDEA会话框的顶部位置
        JLabel title = new JLabel("表单标题");
        // 字体样式
        title.setFont(new Font("微软雅黑", Font.PLAIN, 26));
        // 水平居中
        title.setHorizontalAlignment(SwingConstants.CENTER);
        // 垂直居中
        title.setVerticalAlignment(SwingConstants.CENTER);
        north.add(title);

        return north;
    }

    /**
     * 特别说明：不需要展示SouthPanel要重写返回null，否则IDEA将展示默认的"Cancel"和"OK"按钮
     *
     * @return
     */
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
        // todo
        jsonContent.setText("[{\"a\":1},{\"a\":1},{\"a\":1},{\"a\":1},{\"a\":1},{\"a\":1},{\"a\":1},{\"a\":1},{\"a\":1},{\"a\":1},{\"a\":1},{\"a\":1},{\"a\":1},{\"a\":1},{\"a\":1},{\"a\":1},{\"a\":1},{\"a\":1},{\"a\":1}]");
        //按钮事件绑定
        submit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //获取到name和age
                String jsonStr = jsonContent.getText();
                JSONArray jsonArray = JSONArray.parseArray(jsonStr);
                List<JSONObject> objects = new ArrayList<>();
                Set<String> columns = new LinkedHashSet<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    columns.addAll(object.keySet());
                    objects.add(object);
                }
                TableRunner executor = new TableRunner(project);
                executor.run(columns, objects);
                // 关闭窗口
                doCancelAction();
            }
        });
        return south;
    }

    @Override
    protected JComponent createCenterPanel() {
        final JPanel center = new JPanel();
        // 定义表单的主题，放置到IDEA会话框的中央位置
        // 一个简单的3行2列的表格布局
        center.setLayout(new GridLayout(3, 2));
        // row2：姓名+文本框
        center.add(json);
        center.add(jsonContent);

        return center;
    }
}

