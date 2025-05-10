package com.zj.jsonsql.utils;

import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.content.Content;
import com.zj.jsonsql.ui.AnActionButtonImpl;
import com.zj.jsonsql.ui.dialog.json.JsonEditorField;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import javax.swing.event.AncestorListener;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

/**
 * @author jie.zhou
 */
public class ExecutorUtil {

    /**
     * 返回正在运行的 Executor
     *
     * @param id Executor id
     */
    public static Executor getRunExecutorInstance(String id) {
        return ExecutorRegistry.getInstance().getExecutorById(id);
    }

    /**
     * 设置 Content 的 Disposer
     * 绑定按钮销毁
     *
     * @param content            Content
     * @param anActionButtonList 按钮数组
     */
    public static void setContentDisposerAnActionButtonImpl(Content content, List<AnActionButtonImpl> anActionButtonList) {
        Disposable disposable = new Disposable() {
            @Override
            public void dispose() {

            }
        };
        content.setDisposer(disposable);
        if (CollectionUtils.isEmpty(anActionButtonList)) {
            return;
        }
        for (AnActionButtonImpl anActionButton : anActionButtonList) {
            Disposer.register(disposable, anActionButton);
        }
    }

    public static void removeListener(AbstractButton button) {
        if (Objects.isNull(button)) {
            return;
        }
        ActionListener[] actionListeners = button.getActionListeners();
        if (Objects.isNull(actionListeners)) {
            return;
        }
        for (ActionListener actionListener : actionListeners) {
            button.removeActionListener(actionListener);
        }
    }

    public static void removeListener(ComboBox<Object> rowComboBox) {
        if (Objects.isNull(rowComboBox)) {
            return;
        }
        ActionListener[] actionListeners = rowComboBox.getActionListeners();
        if (Objects.isNull(actionListeners)) {
            return;
        }
        for (ActionListener actionListener : actionListeners) {
            rowComboBox.removeActionListener(actionListener);
        }
    }

    public static void removeListener(JsonEditorField jsonEditorField) {
        if (Objects.isNull(jsonEditorField)) {
            return;
        }
        AncestorListener[] ancestorListeners = jsonEditorField.getAncestorListeners();
        if (Objects.isNull(ancestorListeners)) {
            return;
        }
        for (AncestorListener ancestorListener : ancestorListeners) {
            jsonEditorField.removeAncestorListener(ancestorListener);
        }
    }
}