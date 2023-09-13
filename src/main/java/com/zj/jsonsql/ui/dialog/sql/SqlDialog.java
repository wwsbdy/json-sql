package com.zj.jsonsql.ui.dialog.sql;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;
import com.zj.jsonsql.constant.Constant;
import com.zj.jsonsql.entity.Field;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.enums.NoticeEnum;
import com.zj.jsonsql.utils.SqlUtil;
import org.apache.calcite.sql.SqlSelect;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author arthur_zhou
 */
public class SqlDialog extends DialogWrapper {

    /**
     * swing样式类，定义在4.3.2
     */
    private final JTextPane sqlContent = new JTextPane();
    private final JsonInfo jsonInfo;

    public SqlDialog(JsonInfo jsonInfo) {
        super(true);
        this.jsonInfo = jsonInfo;
        // 设置会话框标题
        setTitle("输入sql");
        // 获取到当前项目的名称
        // 初始化文本框
        initSqlContent();
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

    /**
     * 初始化文本
     */
    private void initSqlContent() {
        sqlContent.setText(jsonInfo.getSql());
        // 创建关键字提示框
        List<String> keywords = jsonInfo.getColumns().stream()
                .map(Field::getOriginalName)
                .collect(Collectors.toList());
        Document document = sqlContent.getDocument();
        JBPopupMenu keywordPopup = new JBPopupMenu();
        keywordPopup.setFocusable(false);
        document.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                showKeywordPopup();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                showKeywordPopup();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                showKeywordPopup();
            }

            private void showKeywordPopup() {
                keywordPopup.setVisible(false);
                keywordPopup.removeAll();
                String text = sqlContent.getText();
                int caretPosition = Math.min(sqlContent.getCaretPosition(), text.length());
                int wordStart = caretPosition;
                int wordEnd = caretPosition;

                while (wordStart > 0 && Character.isJavaIdentifierPart(text.charAt(wordStart - 1))) {
                    wordStart--;
                }
                while (wordEnd < text.length() && Character.isJavaIdentifierPart(text.charAt(wordEnd))) {
                    wordEnd++;
                }
                String word = text.substring(wordStart, wordEnd);
                if (!word.isEmpty()) {
                    for (String keyword : keywords) {
                        if (!keyword.equals(word) && keyword.startsWith(word)) {
                            JMenuItem keywordItem = new JMenuItem(keyword);
                            int finalWordStart = wordStart;
                            int finalWordEnd = wordEnd;
                            keywordItem.addActionListener(e -> {
                                try {
                                    document.remove(finalWordStart, finalWordEnd - finalWordStart);
                                    document.insertString(finalWordStart, keyword, null);
                                } catch (BadLocationException badLocationException) {
                                    badLocationException.printStackTrace();
                                }
                            });
                            keywordPopup.add(keywordItem);
                        }
                    }
                }

                if (keywordPopup.getComponentCount() > 0) {
                    try {
                        Rectangle2D rectangle2D = sqlContent.getUI().modelToView2D(sqlContent, wordStart, Position.Bias.Backward);
                        keywordPopup.show(sqlContent, (int) rectangle2D.getX(), (int) (rectangle2D.getY() + rectangle2D.getHeight()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    keywordPopup.setVisible(false);
                }
            }
        });
    }
}

