package com.zj.demoplugin.form;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParserNotice;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author arthur_zhou
 */

public class FormTestDialog extends DialogWrapper {

    private String projectName;

    /**
     * swing样式类，定义在4.3.2
     */
    private FormTestSwing formTestSwing = new FormTestSwing();

    public FormTestDialog(Project project) {
        super(true);
        // 设置会话框标题
        setTitle("表单测试~~");
        // 获取到当前项目的名称
        this.projectName = project.getName();
        // 触发一下init方法，否则swing样式将无法展示在会话框
        init();
    }

    @Override
    protected JComponent createNorthPanel() {
        // 返回位于会话框north位置的swing样式
        return formTestSwing.initNorth();
    }

    /**
     * 特别说明：不需要展示SouthPanel要重写返回null，否则IDEA将展示默认的"Cancel"和"OK"按钮
     *
     * @return
     */
    @Override
    protected JComponent createSouthPanel() {
        return formTestSwing.initSouth();
    }

    @Override
    protected JComponent createCenterPanel() {
        RSyntaxTextArea inputTextArea = new RSyntaxTextArea(19, 0);
        inputTextArea.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_JSON);
        // 设置高亮主题
        try (InputStream inputStream = this.getClass().getResourceAsStream(
                "/org/fife/ui/rsyntaxtextarea/themes/idea.xml")) {
            Theme theme = Theme.load(inputStream);
            theme.apply(inputTextArea);
        } catch (IOException ignore) {
        }
        // 可折叠
        inputTextArea.setCodeFoldingEnabled(true);
        // 自动缩进
        inputTextArea.setAutoIndentEnabled(true);
        inputTextArea.addParser(new AbstractParser() {
            @Override
            public ParseResult parse(RSyntaxDocument doc, String style) {
                DefaultParseResult result = new DefaultParseResult(this);
                result.addNotice(new DefaultParserNotice(this, "Message", 0));
                return result;
            }
        });
        RTextScrollPane sp = new RTextScrollPane(inputTextArea);
        // 显示行号
        sp.setLineNumbersEnabled(true);
        return sp;
    }
}

