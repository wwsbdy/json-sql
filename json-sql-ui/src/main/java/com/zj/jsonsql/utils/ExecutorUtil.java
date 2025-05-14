package com.zj.jsonsql.utils;

import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.zj.jsonsql.entity.IdeaJsonInfo;
import com.zj.jsonsql.ui.PluginBundle;
import com.zj.jsonsql.ui.dialog.json.JsonEditorField;
import com.zj.jsonsql.ui.table.Table;

import javax.swing.*;
import javax.swing.event.AncestorListener;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Optional;

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
     * @param table 按钮数组
     */
    public static void setContentDisposerAnActionButtonImpl(Content content, Table table) {
        Disposable disposable = new Disposable() {
            @Override
            public void dispose() {
            }
        };
        content.setDisposer(disposable);
        if (Objects.isNull(table)) {
            return;
        }
        Disposer.register(disposable, table);
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

    public static void addContent(IdeaJsonInfo nextIdeaJsonInfo, Project project, ToolWindow toolWindow) {
        Table table = Table.create(project, nextIdeaJsonInfo, toolWindow);
        Content content = ContentFactory.getInstance()
                .createContent(table.getPanel(), PluginBundle.get("tool-window.title") + toolWindow.getContentManager().getContentCount(), false);
        content.setCloseable(true);
        Optional.ofNullable(content.getDisposer())
                .ifPresent(disposable -> Disposer.register(toolWindow.getDisposable(), disposable));
        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().setSelectedContent(content);
        setContentDisposerAnActionButtonImpl(content, table);
        Optional.ofNullable(content.getDisposer())
                .ifPresent(disposable -> Disposer.register(toolWindow.getDisposable(), disposable));
    }
}