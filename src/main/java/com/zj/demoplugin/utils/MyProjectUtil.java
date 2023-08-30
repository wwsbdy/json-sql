package com.zj.demoplugin.utils;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;

public enum MyProjectUtil {
    ;

    public static void setRunning(Project project, String key, boolean value) {
        PropertiesComponent.getInstance(project).setValue(key, value);
    }

    public static boolean getRunning(Project project, String key) {
        return PropertiesComponent.getInstance(project).getBoolean(key);
    }
}