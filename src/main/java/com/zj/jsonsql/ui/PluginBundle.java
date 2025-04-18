package com.zj.jsonsql.ui;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author : jie.zhou
 * @date : 2025/4/18
 */
public class PluginBundle {
    private static final String BUNDLE_NAME = "messages.MessagesBundle";
    private static ResourceBundle bundle;

    static {
        reloadBundle();
    }

    public static void reloadBundle() {
        Locale ideLocale = DynamicBundle.getLocale();
        bundle = ResourceBundle.getBundle(BUNDLE_NAME, ideLocale);
    }

    public static String get(@NotNull String key) {
        return bundle.getString(key);
    }

    public static String get(@NotNull String key, Object... params) {
        return String.format(get(key), params);
    }
}
