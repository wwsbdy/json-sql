package com.zj.jsonsql.ui.dialog.importexcel;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.zj.jsonsql.entity.IdeaJsonInfo;
import com.zj.jsonsql.ui.PluginBundle;
import com.zj.jsonsql.utils.ExecutorUtil;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author : jie.zhou
 * @date : 2025/4/18
 */
@Getter
@Setter
public class ImportDialog extends DialogWrapper {

    private final Project project;
    private final IdeaJsonInfo jsonInfo;

    private boolean disposed = false;
    private TextFieldWithBrowseButton textFieldWithBrowseButton;
    private JButton submit;

    public ImportDialog(Project project, @NotNull IdeaJsonInfo jsonInfo) {
        super(true);
        this.project = project;
        this.jsonInfo = jsonInfo;
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, false);
        textFieldWithBrowseButton = new TextFieldWithBrowseButton();
        textFieldWithBrowseButton.addBrowseFolderListener(new TextBrowseFolderListener(descriptor));
        textFieldWithBrowseButton.setPreferredSize(new Dimension(300, 20));
        setTitle(PluginBundle.get("import.title"));
        // 触发一下init方法，否则swing样式将无法展示在会话框
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return textFieldWithBrowseButton;
    }

    @Override
    protected JComponent createSouthPanel() {
        final JPanel south = new JPanel();
        //定义表单的提交按钮，放置到IDEA会话框的底部位置
        JButton submit = new JButton(PluginBundle.get("import.submit"));
        // 水平居中
        submit.setHorizontalAlignment(SwingConstants.CENTER);
        // 垂直居中
        submit.setVerticalAlignment(SwingConstants.CENTER);
        south.add(submit);
        //按钮事件绑定
        submit.addActionListener(new ImportButtonAction(project, this, jsonInfo));
        this.submit = submit;
        return south;
    }

    @Override
    protected void dispose() {
        if (!disposed) {
            disposed = true;
            if (Objects.nonNull(textFieldWithBrowseButton)) {
                textFieldWithBrowseButton.dispose();
            }
            ExecutorUtil.removeListener(submit);
            textFieldWithBrowseButton = null;
            submit = null;
        }
        super.dispose();
    }
}
