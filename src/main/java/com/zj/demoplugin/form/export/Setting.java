package com.zj.demoplugin.form.export;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.wizard.WizardModel;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import com.zj.demoplugin.entity.ExportInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Setting extends WizardStep<WizardModel> {

    private final ExportInfo exportInfo;

    public Setting(ExportInfo exportInfo) {
        this.exportInfo = exportInfo;
    }

    @Override
    public JComponent prepare(WizardNavigationState state) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(4, 2));
        ComboBox<Object> rowComboBox = new ComboBox<>();
        rowComboBox.addItem("SQL查询");
        rowComboBox.addItem("勾选行");
        rowComboBox.addItem("全部");
        rowComboBox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportInfo.setRow(rowComboBox.getSelectedIndex());
            }
        });

        jPanel.add(new JLabel("导出行"));
        jPanel.add(rowComboBox);

        ComboBox<Object> columnComboBox = new ComboBox<>();
        columnComboBox.addItem("SQL查询");
        columnComboBox.addItem("全部");
        columnComboBox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportInfo.setColumn(columnComboBox.getSelectedIndex());
            }
        });
        jPanel.add(new JLabel("导出列"));
        jPanel.add(columnComboBox);

        JBCheckBox roundCheckBox = new JBCheckBox();
        roundCheckBox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportInfo.setRound(roundCheckBox.isSelected());
            }
        });
        JLabel label = new JLabel("平铺数组");
        label.setToolTipText("<html>仅有一个字段导出时，会把对象平铺成基础值</html>");
        jPanel.add(label);
        jPanel.add(roundCheckBox);
        JBCheckBox beautifyCheckBox = new JBCheckBox();
        beautifyCheckBox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportInfo.setBeautify(beautifyCheckBox.isSelected());
            }
        });
        jPanel.add(new JLabel("美化Json"));
        jPanel.add(beautifyCheckBox);
        JPanel resultPanel = new JPanel(new GridLayout(4, 1));
        resultPanel.setPreferredSize(new Dimension(500, 500));
        resultPanel.add(jPanel);
        return resultPanel;
    }
}
