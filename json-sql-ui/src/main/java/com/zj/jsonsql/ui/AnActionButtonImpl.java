package com.zj.jsonsql.ui;

import com.intellij.openapi.Disposable;
import com.intellij.ui.AnActionButton;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.function.Supplier;

/**
 * @author jie.zhou
 */
public abstract class AnActionButtonImpl extends AnActionButton implements Disposable {
    public AnActionButtonImpl(@Nls(capitalization = Nls.Capitalization.Title) String text) {
        super(text);
    }

    public AnActionButtonImpl(@NotNull Supplier<String> dynamicText) {
        super(dynamicText);
    }

    public AnActionButtonImpl(@Nls(capitalization = Nls.Capitalization.Title) String text, @Nls(capitalization = Nls.Capitalization.Sentence) String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    public AnActionButtonImpl(@NotNull Supplier<String> dynamicText, @NotNull Supplier<String> dynamicDescription, @Nullable Icon icon) {
        super(dynamicText, dynamicDescription, icon);
    }

    public AnActionButtonImpl(@Nls(capitalization = Nls.Capitalization.Title) String text, Icon icon) {
        super(text, icon);
    }

    public AnActionButtonImpl(@NotNull Supplier<String> dynamicText, Icon icon) {
        super(dynamicText, icon);
    }

    public AnActionButtonImpl() {
    }

    @Override
    public void dispose() {

    }
}
