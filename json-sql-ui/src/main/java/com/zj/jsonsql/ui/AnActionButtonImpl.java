package com.zj.jsonsql.ui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.AnActionButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.function.Supplier;

/**
 * @author jie.zhou
 */
public abstract class AnActionButtonImpl extends AnActionButton implements Disposable {
    public AnActionButtonImpl(@NlsContexts.Button String text) {
        super(text);
    }

    public AnActionButtonImpl(@NotNull Supplier<String> dynamicText) {
        super(dynamicText);
    }

    public AnActionButtonImpl(@NlsContexts.Button String text, @NlsContexts.Tooltip String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    public AnActionButtonImpl(@NotNull Supplier<String> dynamicText, @NotNull Supplier<String> dynamicDescription, @Nullable Icon icon) {
        super(dynamicText, dynamicDescription, icon);
    }

    public AnActionButtonImpl(@NlsContexts.Button String text, Icon icon) {
        super(text, icon);
    }

    public AnActionButtonImpl(@NotNull Supplier<String> dynamicText, Icon icon) {
        super(dynamicText, icon);
    }

    public AnActionButtonImpl() {
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    @Override
    public void dispose() {

    }
}
