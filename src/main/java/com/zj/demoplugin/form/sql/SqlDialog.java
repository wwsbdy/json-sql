package com.zj.demoplugin.form.sql;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;
import com.zj.demoplugin.constant.Constant;
import com.zj.demoplugin.entity.JsonInfo;
import com.zj.demoplugin.enums.NoticeEnum;
import com.zj.demoplugin.utils.SqlUtil;
import org.apache.calcite.sql.SqlSelect;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * @author arthur_zhou
 */

public class SqlDialog extends DialogWrapper {

    private Project project;

    /**
     * swing样式类，定义在4.3.2
     */
    private final JTextPane sqlContent = new JTextPane();
    private final JsonInfo jsonInfo;

    public SqlDialog(Project project, JsonInfo jsonInfo) {
        super(true);
        this.jsonInfo = jsonInfo;
        // 设置会话框标题
        setTitle("输入sql");
        sqlContent.setText(jsonInfo.getSql());
        // 获取到当前项目的名称
        this.project = project;
        // 触发一下init方法，否则swing样式将无法展示在会话框
        init();
    }

    @Override
    protected JComponent createNorthPanel() {
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
                String sqlStr = sqlContent.getText();
                if (StringUtils.isNotEmpty(sqlStr) && sqlStr.length() > Constant.SQL_MAX) {
                    Messages.showErrorDialog(NoticeEnum.SQL_TOO_LONG.getMessage(), NoticeEnum.SQL_TOO_LONG.getWarn());
                } else {
                    SqlSelect sqlSelect = SqlUtil.toSqlSelect(sqlStr);
                    if (Objects.isNull(sqlSelect)) {
                        Messages.showErrorDialog(NoticeEnum.SQL_ERROR.getMessage(), NoticeEnum.SQL_ERROR.getWarn());
                    } else {
                        jsonInfo.setSql(sqlStr);
                        jsonInfo.setSqlNode(sqlSelect);
                        // 关闭窗口
                        doCancelAction();
                    }
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
        center.setPreferredSize(new Dimension(500, 250));
        panel1.add(center, BorderLayout.CENTER);
        final JBScrollPane scrollPane1 = new JBScrollPane();
        center.add(scrollPane1, BorderLayout.CENTER);
        scrollPane1.setViewportView(sqlContent);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panel1.add(panel2, BorderLayout.SOUTH);
        return contentPanel;
    }
}

