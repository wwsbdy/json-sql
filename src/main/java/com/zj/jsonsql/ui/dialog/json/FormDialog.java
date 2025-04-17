package com.zj.jsonsql.ui.dialog.json;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.ui.edit.CustomEditorField;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author arthur_zhou
 */
@Setter
@Getter
public class FormDialog extends DialogWrapper {

    private final Project project;

    /**
     * swing样式类，定义在4.3.2
     */
    private final CustomEditorField jsonContent;

    private JsonInfo jsonInfo;

    public FormDialog(Project project, JsonInfo jsonInfo) {
        super(true);
        // 是否允许拖拽的方式扩大或缩小
        setResizable(true);
        this.jsonInfo = jsonInfo;
        String content = jsonInfo.getJsonContent();
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

    @Deprecated
    public FormDialog(Project project, String content) {
        this(project, new JsonInfo(content));
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
        submit.addActionListener(new ButtonAction(this));
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

