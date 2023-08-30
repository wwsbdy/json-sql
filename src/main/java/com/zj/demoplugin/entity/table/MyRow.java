package com.zj.demoplugin.entity.table;

import com.intellij.ide.browsers.BrowserSpecificSettings;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.PathUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MyRow extends AbstractRow {

    private final UUID id;

    @NotNull
    private MyIconItem icon;

    @NotNull
    private String name;

    private boolean active;

    private String path;

    /**
     * 保存当前表格数据
     */
    private BrowserSpecificSettings specificSettings;

    @SuppressWarnings("UnusedDeclaration")
    MyRow() {
        this(UUID.randomUUID(), MyIconItem.CHROME);
    }

    MyRow(@NotNull UUID id, @NotNull MyIconItem icon) {
        this(id, icon, icon.getName(), icon.getExecutionPath(), true, icon.createBrowserSpecificSettings());
    }

    public MyRow(@NotNull UUID id,
                 @NotNull MyIconItem icon,
                 @NotNull String name,
                 @Nullable String path,
                 boolean active,
                 @Nullable BrowserSpecificSettings specificSettings) {
        this.id = id;
        this.icon = icon;
        this.name = name;

        this.path = StringUtil.nullize(path);
        this.active = active;
        this.specificSettings = specificSettings;
    }

    public void setName(@NotNull String value) {
        name = value;
    }

    public void setIcon(@NotNull MyIconItem value) {
        icon = value;
    }

    @Nullable
    @Override
    public String getPath() {
        return path;
    }

    public void setPath(@Nullable String value) {
        path = PathUtil.toSystemIndependentName(StringUtil.nullize(value));
    }

    @Override
    @Nullable
    public BrowserSpecificSettings getSpecificSettings() {
        return specificSettings;
    }

    public void setSpecificSettings(@Nullable BrowserSpecificSettings value) {
        specificSettings = value;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean value) {
        active = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyRow)) {
            return false;
        }

        MyRow browser = (MyRow) o;
        return getId().equals(browser.getId()) &&
                icon.equals(browser.icon) &&
                active == browser.active &&
                Comparing.strEqual(name, browser.name) &&
                Comparing.equal(path, browser.path) &&
                Comparing.equal(specificSettings, browser.specificSettings);
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    @NotNull
    public String getName() {
        return name;
    }

    @Override
    @NotNull
    public final UUID getId() {
        return id;
    }

    @Override
    @NotNull
    public MyIconItem getIcon() {
        return icon;
    }


    @Override
    public String toString() {
        return getName() + " (" + getPath() + ")";
    }
}