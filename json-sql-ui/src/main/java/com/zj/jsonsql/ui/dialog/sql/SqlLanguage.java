package com.zj.jsonsql.ui.dialog.sql;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jie.zhou
 * @date : 2025/5/7
 */
public class SqlLanguage extends Language {
    public static final SqlLanguage INSTANCE = new SqlLanguage();

    private SqlLanguage() {
        super("SQL-CUSTOM", "text/plain");
    }



    @Override
    public @NotNull String getDisplayName() {
        return "SQL-CUSTOM";
    }
}
