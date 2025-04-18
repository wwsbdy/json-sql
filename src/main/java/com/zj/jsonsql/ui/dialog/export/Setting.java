package com.zj.jsonsql.ui.dialog.export;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.wizard.WizardModel;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import com.zj.jsonsql.entity.ExportInfo;
import com.zj.jsonsql.ui.PluginBundle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author arthur_zhou
 */
public class Setting extends WizardStep<WizardModel> {

    private final ExportInfo exportInfo;

    public Setting(ExportInfo exportInfo) {
        this.exportInfo = exportInfo;
    }

    @Override
    public JComponent prepare(WizardNavigationState state) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(5, 2));
        ComboBox<Object> rowComboBox = new ComboBox<>();
        rowComboBox.addItem(PluginBundle.get("setting.sql-query"));
        rowComboBox.addItem(PluginBundle.get("setting.select-row"));
        rowComboBox.addItem(PluginBundle.get("setting.all"));
        rowComboBox.setSelectedIndex(exportInfo.getRow());
        rowComboBox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportInfo.setRow(rowComboBox.getSelectedIndex());
            }
        });

        jPanel.add(new JBLabel(PluginBundle.get("setting.export-row")));
        jPanel.add(rowComboBox);

        ComboBox<Object> columnComboBox = new ComboBox<>();
        columnComboBox.addItem(PluginBundle.get("setting.sql-query"));
        columnComboBox.addItem(PluginBundle.get("setting.all"));
        columnComboBox.setSelectedIndex(exportInfo.getColumn());
        columnComboBox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportInfo.setColumn(columnComboBox.getSelectedIndex());
            }
        });
        jPanel.add(new JBLabel(PluginBundle.get("setting.export-column")));
        jPanel.add(columnComboBox);

        JBCheckBox roundCheckBox = new JBCheckBox();
        roundCheckBox.setSelected(exportInfo.isRound());
        roundCheckBox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportInfo.setRound(roundCheckBox.isSelected());
            }
        });
        JBLabel label = new JBLabel(PluginBundle.get("setting.flat-array"));
        label.setToolTipText(PluginBundle.get("setting.flat-array-desc"));
        jPanel.add(label);
        jPanel.add(roundCheckBox);
        JBCheckBox beautifyCheckBox = new JBCheckBox();
        beautifyCheckBox.setSelected(exportInfo.isBeautify());
        beautifyCheckBox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportInfo.setBeautify(beautifyCheckBox.isSelected());
            }
        });
        jPanel.add(new JBLabel(PluginBundle.get("setting.beautify-json")));
        jPanel.add(beautifyCheckBox);
        JBCheckBox distinctCheckBox = new JBCheckBox();
        distinctCheckBox.setSelected(exportInfo.isDistinct());
        distinctCheckBox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportInfo.setDistinct(beautifyCheckBox.isSelected());
            }
        });
        jPanel.add(new JBLabel(PluginBundle.get("setting.deduplication")));
        jPanel.add(distinctCheckBox);
        JPanel resultPanel = new JPanel(new GridLayout(4, 1));
        resultPanel.setPreferredSize(new Dimension(500, 500));
        resultPanel.add(jPanel);
        return resultPanel;
    }
}
