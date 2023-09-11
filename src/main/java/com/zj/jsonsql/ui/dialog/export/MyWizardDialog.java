package com.zj.jsonsql.ui.dialog.export;

import com.intellij.openapi.project.Project;
import com.intellij.ui.wizard.WizardDialog;
import com.intellij.ui.wizard.WizardModel;
import com.zj.jsonsql.entity.ExportInfo;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.utils.JsonUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * 自定义的向导对话框
 *
 * @author 19242
 */
public class MyWizardDialog extends WizardDialog<WizardModel> {

    private final ExportInfo exportInfo;
    private final JsonInfo jsonInfo;

    public MyWizardDialog(ExportInfo exportInfo, JsonInfo jsonInfo, Project project, boolean canBeParent, WizardModel model) {
        super(project, canBeParent, model);
        this.jsonInfo = jsonInfo;
        setResizable(false);
        this.exportInfo = exportInfo;
    }

    @Override
    protected JComponent createSouthPanel() {
        JComponent oldSouthPanel = super.createSouthPanel();
        // 只要上一步和下一步
        Component[] components = oldSouthPanel.getComponents();
        final JPanel panel1 = new JPanel(new GridLayout(1, 0, 5, 0));
        if (Objects.nonNull(components) && components.length > 0) {
            JPanel panel = (JPanel) components[0];
            panel1.add(panel.getComponent(0));
            JButton jButton = (JButton) panel.getComponent(0);
            jButton.addActionListener(event -> exportInfo.setJsonArrayStr(JsonUtil.getJsonStr(jsonInfo, exportInfo)));
            panel1.add(jButton);
        }
        final JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(panel1, BorderLayout.EAST);
        southPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        return southPanel;
    }
//    @Override
//    protected Dimension getWindowPreferredSize() {
//        // 设置首选大小
//        return new Dimension(400, 300);
//    }
}
