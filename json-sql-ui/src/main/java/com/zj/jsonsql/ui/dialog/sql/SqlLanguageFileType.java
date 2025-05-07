package com.zj.jsonsql.ui.dialog.sql;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author : jie.zhou
 * @date : 2025/5/7
 */
public class SqlLanguageFileType extends LanguageFileType {

    public static final @NotNull FileType INSTANCE = new SqlLanguageFileType();
    protected SqlLanguageFileType() {
        super(SqlLanguage.INSTANCE);
    }

    @Override
    public @NonNls @NotNull String getName() {
        return "SQL-CUSTOM";
    }

    @Override
    public @Nls @NotNull String getDescription() {
        return "SQL-CUSTOM";
    }

    @Override
    public @NonNls @NotNull String getDefaultExtension() {
        return "SQL-CUSTOM";
    }

    @Override
    public Icon getIcon() {
        return null;
    }
}
