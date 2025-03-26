package com.zj.jsonsql.ui.dialog.export;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.wizard.WizardModel;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import com.zj.jsonsql.entity.ExportInfo;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.enums.NoticeEnum;
import com.zj.jsonsql.utils.EasyExcelUtil;
import com.zj.jsonsql.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author arthur_zhou
 * @date 2023/12/22 17:50
 */
@Slf4j
public class Export extends WizardStep<WizardModel> {

    private final ExportInfo exportInfo;
    private final Project project;
    private final JsonInfo jsonInfo;

    public Export(ExportInfo exportInfo, Project project, JsonInfo jsonInfo) {
        this.exportInfo = exportInfo;
        this.project = project;
        this.jsonInfo = jsonInfo;
    }

    @Override
    public JComponent prepare(WizardNavigationState state) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(4, 2));
        FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        TextFieldWithBrowseButton textFieldWithBrowseButton = new TextFieldWithBrowseButton();
        textFieldWithBrowseButton.addBrowseFolderListener(new TextBrowseFolderListener(descriptor));
        textFieldWithBrowseButton.setText(Paths.get(System.getProperty("user.home"), "Desktop").toString());
        jPanel.add(new JBLabel("文件路径"));
        jPanel.add(textFieldWithBrowseButton);

        ComboBox<ExcelTypeEnum> rowComboBox = new ComboBox<>();
        rowComboBox.addItem(ExcelTypeEnum.XLSX);
        rowComboBox.addItem(ExcelTypeEnum.XLS);
        rowComboBox.addItem(ExcelTypeEnum.CSV);
        rowComboBox.setSelectedIndex(0);

        jPanel.add(new JBLabel("格式"));
        jPanel.add(rowComboBox);

        JBTextField fileNameTextFiled = new JBTextField();
        jPanel.add(new JBLabel("文件名"));
        jPanel.add(fileNameTextFiled);

        jPanel.add(new JBLabel());
        JButton exportButton = new JButton("导出");
        exportButton.addActionListener(event -> {
            String path = textFieldWithBrowseButton.getText();
            if (StringUtils.isEmpty(path)) {
                Messages.showErrorDialog(project, NoticeEnum.FOLDER_EMPTY.getMessage(), NoticeEnum.FOLDER_EMPTY.getWarn());
                return;
            }
            String fileName = fileNameTextFiled.getText();
            if (StringUtils.isEmpty(fileName)) {
                Messages.showErrorDialog(project, NoticeEnum.FILE_NAME_ERROR.getMessage(), NoticeEnum.FILE_NAME_ERROR.getWarn());
                return;
            }
            ExcelTypeEnum excelTypeEnum = (ExcelTypeEnum) rowComboBox.getSelectedItem();
            if (Objects.isNull(excelTypeEnum)) {
                Messages.showErrorDialog(project, NoticeEnum.FILE_TYPE_ERROR.getMessage(), NoticeEnum.FILE_TYPE_ERROR.getWarn());
                return;
            }
            String fileUrl = path + File.separator + fileName + excelTypeEnum.getValue().toLowerCase();
            // 不平铺数组
            ExportInfo exportInfo = new ExportInfo(this.exportInfo);
            exportInfo.setRound(false);
            exportInfo.setOnlyOne(false);
            String jsonArrayStr = JsonUtil.getJsonStr(jsonInfo, exportInfo);
            try {
                FileOutputStream out = new FileOutputStream(fileUrl);
                EasyExcelUtil.write(jsonArrayStr, out, excelTypeEnum);
                int i = Messages.showYesNoDialog("是否打开文件", "导出成功", AllIcons.Actions.Commit);
                if (i == Messages.YES) {
                    Desktop.getDesktop().open(new File(fileUrl));
                }
            } catch (FileNotFoundException e) {
                log.error("FileNotFoundException：", e);
                Messages.showErrorDialog(project, e.getMessage(), NoticeEnum.FILE_EXPORT_FAIL.getWarn());
            } catch (Exception e) {
                log.error("Exception：", e);
                Messages.showErrorDialog(project, NoticeEnum.FILE_EXPORT_FAIL.getMessage(), NoticeEnum.FILE_EXPORT_FAIL.getWarn());
            }
        });
        jPanel.add(exportButton);

        JPanel resultPanel = new JPanel(new GridLayout(5, 1));
        resultPanel.add(jPanel);
        return resultPanel;
    }
}
