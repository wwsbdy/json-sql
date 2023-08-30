package com.zj.demoplugin.form;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;

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
        // 定义表单的主题，放置到IDEA会话框的中央位置
        return formTestSwing.initCenter();
    }
}

