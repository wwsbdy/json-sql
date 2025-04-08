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
import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.enums.NoticeEnum;
import com.zj.jsonsql.exception.SqlException;
import com.zj.jsonsql.utils.SqlUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author arthur_zhou
 */
@Slf4j
@Getter
public class SqlDialog extends DialogWrapper {

    /**
     * swing样式类，定义在4.3.2
     */
    private final JTextPane sqlContent = new JTextPane();
    private final JsonInfo jsonInfo;
    private final String originalSql;
    private int currentIndex = -1;
    private final List<String> sqlKeywords = Stream.of("select", "as", "from", "where", "not", "in", "like", "null", "between",
            "is", "and", "or", "order", "by", "asc", "desc", "distinct", "limit", "group").collect(Collectors.toList());

    public SqlDialog(JsonInfo jsonInfo) {
        super(true);
        // 添加函数提示词
        sqlKeywords.addAll(Stream.of(FuncEnum.values()).map(v -> v.name().toLowerCase()).collect(Collectors.toList()));
        this.jsonInfo = jsonInfo;
        this.originalSql = jsonInfo.getSql();
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
                    return;
                }
                SqlSelect sqlSelect;
                try {
                    sqlSelect = SqlUtil.toSqlSelect(sqlStr);
                } catch (SqlParseException e) {
                    log.error("SqlParseException：", e);
                    Messages.showErrorDialog(SqlUtil.getErrorMessage(e), NoticeEnum.SQL_ERROR.getWarn());
                    return;
                }
                if (Objects.isNull(sqlSelect)) {
                    Messages.showErrorDialog(NoticeEnum.SQL_ERROR.getMessage(), NoticeEnum.SQL_ERROR.getWarn());
                    return;
                }
                try {
                    List<Field> fields = SqlUtil.getFields(jsonInfo.getColumns(), sqlSelect);
                    jsonInfo.setSelect(fields);
                    List<Row> result = SqlUtil.getDataList(jsonInfo.getList(), jsonInfo.getColumns(), sqlSelect);
                    jsonInfo.setResult(result);
                } catch (SqlException sqlException) {
                    Messages.showErrorDialog(sqlException.getMessage(), "SQL错误");
                    return;
                }
                jsonInfo.setSql(sqlStr);
                jsonInfo.setSqlNode(sqlSelect);
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
                .map(Field::getOriginalFiled)
                .map(SqlNode::toString)
                .collect(Collectors.toList());
        Document document = sqlContent.getDocument();
        JBPopupMenu keywordPopup = new JBPopupMenu();
        keywordPopup.setFocusable(false);
        // 模糊匹配关键词，添加到提示列表里
        document.addDocumentListener(new MyDocumentListener(keywordPopup, keywords, document));
        // 添加键盘监听器，处理箭头和回车键选择
        sqlContent.addKeyListener(new MyKeyAdapter(keywordPopup, document));
    }

    private class MyKeyAdapter extends KeyAdapter {
        private final JBPopupMenu keywordPopup;
        private final Document document;

        public MyKeyAdapter(JBPopupMenu keywordPopup, Document document) {
            this.keywordPopup = keywordPopup;
            this.document = document;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (keywordPopup.isVisible()) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    currentIndex = (currentIndex + 1) % keywordPopup.getComponentCount();
                    highlightSuggestion();
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    currentIndex = (currentIndex - 1 + keywordPopup.getComponentCount()) % keywordPopup.getComponentCount();
                    highlightSuggestion();
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    insertSuggestion();
                    e.consume();
                }
            }
        }

        // 高亮当前选择的建议
        private void highlightSuggestion() {
            for (int i = 0; i < keywordPopup.getComponentCount(); i++) {
                JMenuItem item = (JMenuItem) keywordPopup.getComponent(i);
                item.setArmed(i == currentIndex);
            }
        }

        // 插入选中的建议
        private void insertSuggestion() {
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
            if (currentIndex < keywordPopup.getComponentCount()) {
                JMenuItem selectedItem = (JMenuItem) keywordPopup.getComponent(Math.max(0, currentIndex));
                String keyword = selectedItem.getText();
                try {
                    document.remove(wordStart, wordEnd - wordStart);
                    document.insertString(wordStart, keyword, null);
                    keywordPopup.setVisible(false);
                } catch (BadLocationException e) {
                    log.error("BadLocationException：", e);
                }
            }
        }

    }

    private class MyDocumentListener implements DocumentListener {
        private final JBPopupMenu keywordPopup;
        private final List<String> keywords;
        private final Document document;

        public MyDocumentListener(JBPopupMenu keywordPopup, List<String> keywords, Document document) {
            this.keywordPopup = keywordPopup;
            this.keywords = keywords;
            this.document = document;
        }

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
            if (StringUtils.isEmpty(word)) {
                return;
            }
            Set<String> keywordSet = getStrings(word);
            for (String keyword : keywordSet) {
                JMenuItem keywordItem = getMenuItem(keyword, wordStart, wordEnd);
                keywordPopup.add(keywordItem);
            }
            if (keywordPopup.getComponentCount() > 0) {
                try {
                    currentIndex = -1;
                    Rectangle2D rectangle2D = sqlContent.getUI().modelToView2D(sqlContent, wordEnd, Position.Bias.Forward);
                    keywordPopup.show(sqlContent, (int) rectangle2D.getX(), (int) (rectangle2D.getY() + rectangle2D.getHeight()));
                } catch (Exception ex) {
                    log.error("Error showing keyword popup: ", ex);
                }
            } else {
                keywordPopup.setVisible(false);
            }
        }

        private @NotNull Set<String> getStrings(String word) {
            Set<String> keywordSet = new LinkedHashSet<>();
            for (String keyword : keywords) {
                if (!keyword.equals(word) && keyword.startsWith(word)) {
                    keywordSet.add(keyword);
                }
            }
            // 添加sql关键字
            String wordLowerCase = word.toLowerCase();
            for (String keyword : sqlKeywords) {
                if (!keyword.equals(wordLowerCase) && keyword.startsWith(wordLowerCase)) {
                    keywordSet.add(keyword);
                }
            }
            return keywordSet;
        }

        private @NotNull JMenuItem getMenuItem(String keyword, int wordStart, int wordEnd) {
            JMenuItem keywordItem = new JMenuItem(keyword);
            keywordItem.addActionListener(e -> {
                try {
                    document.remove(wordStart, wordEnd - wordStart);
                    document.insertString(wordStart, keyword, null);
                } catch (BadLocationException badLocationException) {
                    log.error("Error removing text: ", badLocationException);
                }
            });
            return keywordItem;
        }
    }
}

