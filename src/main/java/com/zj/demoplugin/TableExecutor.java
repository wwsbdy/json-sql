package com.zj.demoplugin;

import com.intellij.execution.Executor;
import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

/**
 * url: https://blog.csdn.net/q258523454/article/details/123097368
 * @author arthur_zhou
 */
public class TableExecutor extends Executor {


    public static final String PLUGIN_ID = "TableExecutor";

    public static final String TOOL_WINDOW_ID = "TableExecutor";

    public static final String CONTEXT_ACTION_ID = "2222";

    @NotNull
    @Override
    public String getId() {
        return PLUGIN_ID;
    }

    @Override
    public String getToolWindowId() {
        return TOOL_WINDOW_ID;
    }

    @Override
    public Icon getToolWindowIcon() {
        return AllIcons.Actions.Redo;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return AllIcons.Actions.Redo;
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
