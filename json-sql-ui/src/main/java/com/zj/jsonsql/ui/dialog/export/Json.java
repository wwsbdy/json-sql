package com.zj.jsonsql.ui.dialog.export;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.AncestorListenerAdapter;
import com.intellij.ui.wizard.WizardModel;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;
import com.zj.jsonsql.entity.ExportInfo;
import com.zj.jsonsql.ui.dialog.json.JsonLanguage;
import com.zj.jsonsql.ui.dialog.json.JsonEditorField;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import java.awt.*;

/**
 * @author 19242
 */
public class Json extends WizardStep<WizardModel> implements Disposable {

    private final ExportInfo exportInfo;
    private final Project project;

    public Json(ExportInfo exportInfo, Project project) {
        this.exportInfo = exportInfo;
        this.project = project;
    }

    @Override
    public JComponent prepare(WizardNavigationState state) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayoutManager(1, 1, JBUI.emptyInsets(), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        jPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel center = new JPanel();
        center.setLayout(new BorderLayout(0, 0));
        center.setPreferredSize(new Dimension(500, 250));
        panel1.add(center, BorderLayout.CENTER);
        JsonEditorField jsonEditorField = new JsonEditorField(JsonLanguage.INSTANCE, project, "");
        jsonEditorField.addAncestorListener(new AncestorListenerAdapter() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                jsonEditorField.setText(exportInfo.getJsonArrayStr());
            }
        });
        Disposer.register(this, jsonEditorField);
        center.add(jsonEditorField, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panel1.add(panel2, BorderLayout.SOUTH);
        return jPanel;
    }

    @Override
    public void dispose() {

    }
}
