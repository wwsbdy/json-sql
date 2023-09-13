package com.zj.jsonsql.ui.table;

import com.intellij.execution.Executor;
import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

/**
 * @author arthur_zhou
 */
public class TableExecutor extends Executor {


    public static final String PLUGIN_ID = "JsonSql";

    public static final String TOOL_WINDOW_ID = "JsonSql";

    public static final String CONTEXT_ACTION_ID = "2222";

    @NotNull
    @Override
    public String getId() {
        return PLUGIN_ID;
    }

    @Override
    public @NotNull String getToolWindowId() {
        return TOOL_WINDOW_ID;
    }

    @Override
    public @NotNull Icon getToolWindowIcon() {
        return AllIcons.FileTypes.Json;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return AllIcons.Actions.Show;
    }

    @Override
    public Icon getDisabledIcon() {
        return AllIcons.Actions.Redo;
    }

    @Override
    public String getDescription() {
        return TOOL_WINDOW_ID;
    }

    @NotNull
    @Override
    public String getActionName() {
        return TOOL_WINDOW_ID;
    }

    @NotNull
    @Override
    public String getStartActionText() {
        return TOOL_WINDOW_ID;
    }

    @Override
    public String getContextActionId() {
        return CONTEXT_ACTION_ID;
    }

    @Override
    public String getHelpId() {
        return TOOL_WINDOW_ID;
    }

}
