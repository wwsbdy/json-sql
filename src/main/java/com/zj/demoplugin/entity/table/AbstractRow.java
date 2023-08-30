package com.zj.demoplugin.entity.table;

import com.intellij.ide.browsers.BrowserSpecificSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class AbstractRow {
    public AbstractRow() {
    }

    @NotNull
    public abstract UUID getId();

    @NotNull
    public abstract String getName();


    @NotNull
    public abstract MyIconItem getIcon();

    @Nullable
    public abstract String getPath();

    @Nullable
    public abstract BrowserSpecificSettings getSpecificSettings();
}