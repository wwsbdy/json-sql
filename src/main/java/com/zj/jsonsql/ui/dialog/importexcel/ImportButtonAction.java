package com.zj.jsonsql.ui.dialog.importexcel;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.enums.NoticeEnum;
import com.zj.jsonsql.ui.ButtonAction;
import com.zj.jsonsql.utils.EasyExcelUtil;
import org.apache.commons.lang3.StringUtils;

import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * @author : jie.zhou
 * @date : 2025/4/18
 */
public class ImportButtonAction extends ButtonAction<ImportDialog> {
    public ImportButtonAction(Project project, ImportDialog dialog, JsonInfo jsonInfo) {
        super(project, dialog, jsonInfo);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TextFieldWithBrowseButton textFieldWithBrowseButton = dialog.getTextFieldWithBrowseButton();
        String filePath = textFieldWithBrowseButton.getText();
        if (StringUtils.isEmpty(filePath)) {
            Messages.showErrorDialog(project, NoticeEnum.FILE_PATH_ERROR.getMessage(), NoticeEnum.FILE_PATH_ERROR.getWarn());
            return;
        }
        String fileSuffix = filePath.toLowerCase();
        if (!fileSuffix.endsWith(ExcelTypeEnum.XLSX.getValue())
                && !fileSuffix.endsWith(ExcelTypeEnum.XLS.getValue())
                && !fileSuffix.endsWith(ExcelTypeEnum.CSV.getValue())) {
            Messages.showErrorDialog(project, NoticeEnum.FILE_TYPE_ERROR.getMessage(), NoticeEnum.FILE_TYPE_ERROR.getWarn());
            return;
        }
        JSONArray jsonArray;
        try {
            jsonArray = EasyExcelUtil.read(filePath);
        } catch (Exception ex) {
            Messages.showErrorDialog(project, NoticeEnum.FILE_READ_ERROR.getMessage(), NoticeEnum.FILE_READ_ERROR.getWarn());
            return;
        }
        if (Objects.isNull(jsonArray)) {
            jsonArray = new JSONArray();
        }
        end(jsonArray, jsonArray.toString(SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue));
    }

}
