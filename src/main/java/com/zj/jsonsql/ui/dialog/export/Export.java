package com.zj.jsonsql.ui.dialog.export;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.wizard.WizardModel;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import com.zj.jsonsql.entity.ExportInfo;

import javax.swing.*;
import java.awt.*;

/**
 * @author arthur_zhou
 * @date 2023/12/22 17:50
 */
public class Export extends WizardStep<WizardModel> {

    private final ExportInfo exportInfo;
    private final Project project;

    public Export(ExportInfo exportInfo, Project project) {
        this.exportInfo = exportInfo;
        this.project = project;
    }

    @Override
    public JComponent prepare(WizardNavigationState state) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(2, 2));
        FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        TextFieldWithBrowseButton textFieldWithBrowseButton = new TextFieldWithBrowseButton();
        textFieldWithBrowseButton.addBrowseFolderListener(new TextBrowseFolderListener(descriptor));
        jPanel.add(new JLabel("文件路径"));
        jPanel.add(textFieldWithBrowseButton);

        ComboBox<Object> rowComboBox = new ComboBox<>();
        rowComboBox.addItem("xlsx");
        rowComboBox.addItem("xls");
        rowComboBox.addItem("csv");
        rowComboBox.setSelectedIndex(0);

        jPanel.add(new JLabel("格式"));
        jPanel.add(rowComboBox);

        JPanel resultPanel = new JPanel(new GridLayout(10, 1));
        resultPanel.add(jPanel);

        JButton exportButton = new JButton("导出");
        exportButton.setPreferredSize(new Dimension(2, 2));
        exportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPanel.add(exportButton);
        return resultPanel;
    }
}
