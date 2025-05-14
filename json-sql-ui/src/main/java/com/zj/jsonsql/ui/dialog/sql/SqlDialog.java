package com.zj.jsonsql.ui.dialog.sql;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;
import com.zj.jsonsql.entity.IdeaJsonInfo;
import com.zj.jsonsql.enums.NoticeEnum;
import com.zj.jsonsql.exception.SqlException;
import com.zj.jsonsql.ui.PluginBundle;
import com.zj.jsonsql.utils.ExecutorUtil;
import com.zj.jsonsql.utils.SqlUtil;
import lombok.Getter;
import org.apache.calcite.sql.parser.SqlParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * @author arthur_zhou
 */
@Getter
public class SqlDialog extends DialogWrapper {

    /**
     * swing样式类，定义在4.3.2
     */
    private final SqlEditorFiled sqlContent;
    private final IdeaJsonInfo jsonInfo;
    private final String originalSql;

    private boolean disposed = false;
    private JButton submit;
    private ComboBox<Object> rowComboBox;

    public SqlDialog(IdeaJsonInfo jsonInfo, Project project) {
        super(true);
        this.jsonInfo = jsonInfo;
        this.originalSql = jsonInfo.getSql();
        // 设置会话框标题
        setTitle(PluginBundle.get("sql.title"));
        sqlContent = new SqlEditorFiled(SqlLanguage.INSTANCE, project, jsonInfo);
        Disposer.register(getDisposable(), sqlContent);
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
        south.setLayout(new GridLayout(0, 3));
        south.add(new JBLabel());
        //定义表单的提交按钮，放置到IDEA会话框的底部位置
        JButton submit = new JButton(PluginBundle.get("sql.submit"));
        // 水平居中
        submit.setHorizontalAlignment(SwingConstants.CENTER);
        // 垂直居中
        submit.setVerticalAlignment(SwingConstants.CENTER);
        south.add(submit);
        //按钮事件绑定
        submit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String sqlStr = sqlContent.getText();
//                if (StringUtils.isNotEmpty(sqlStr) && sqlStr.length() > Constant.SQL_MAX) {
//                    Messages.showErrorDialog(NoticeEnum.SQL_TOO_LONG.getMessage(), NoticeEnum.SQL_TOO_LONG.getWarn());
//                    return;
//                }
                try {
                    jsonInfo.trySql(sqlStr);
                } catch (SqlParseException e) {
                    Messages.showErrorDialog(SqlUtil.getErrorMessage(e), NoticeEnum.SQL_ERROR.getWarn());
                    return;
                } catch (SqlException sqlException) {
                    Messages.showErrorDialog(sqlException.getMessage(), PluginBundle.get("error.title.sql-error"));
                    return;
                } catch (Exception e) {
                    Messages.showErrorDialog(NoticeEnum.SQL_ERROR.getMessage(), NoticeEnum.SQL_ERROR.getWarn());
                    return;
                }
                // 关闭窗口
                doCancelAction();
            }
        });
        if (jsonInfo.getSqlHistoryList().isNotEmpty()) {
            ComboBox<Object> rowComboBox = new ComboBox<>();
            jsonInfo.getSqlHistoryList().forEach(rowComboBox::addItem);
            rowComboBox.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (Objects.isNull(rowComboBox.getSelectedItem())) {
                        return;
                    }
                    sqlContent.setText(rowComboBox.getSelectedItem().toString());
                }
            });
            south.add(rowComboBox);
            this.rowComboBox = rowComboBox;
        } else {
            south.add(new JBLabel());
        }
        this.submit = submit;

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

    @Override
    protected void dispose() {
        if (!disposed) {
            disposed = true;
            ExecutorUtil.removeListener(submit);
            ExecutorUtil.removeListener(rowComboBox);
            submit = null;
            rowComboBox = null;
        }
        super.dispose();
    }
}

