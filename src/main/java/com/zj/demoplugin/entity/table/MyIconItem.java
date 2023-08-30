package com.zj.demoplugin.entity.table;

import com.intellij.icons.AllIcons.Xml.Browsers;
import com.intellij.ide.browsers.BrowserSpecificSettings;
import com.intellij.ide.browsers.chrome.ChromeSettings;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

/**
 * @author arthur_zhou
 */

public enum MyIconItem implements Iconable {
    CHROME("chrome", "chrome", "google-chrome", "Google Chrome", Browsers.Chrome16) {
        @Override
        public BrowserSpecificSettings createBrowserSpecificSettings() {
            return new ChromeSettings();
        }
    },
    ;

    private final String myName;
    private final String myWindowsPath;
    private final String myUnixPath;
    private final String myMacPath;
    private final Icon myIcon;

    private MyIconItem(@NotNull String name, @NotNull String windowsPath, @Nullable String unixPath, @Nullable String macPath, @NotNull Icon icon) {
        this.myName = name;
        this.myWindowsPath = windowsPath;
        this.myUnixPath = unixPath;
        this.myMacPath = macPath;
        this.myIcon = icon;
    }

    @Nullable
    public BrowserSpecificSettings createBrowserSpecificSettings() {
        return null;
    }

    @Nullable
    public String getExecutionPath() {
        if (SystemInfo.isWindows) {
            return this.myWindowsPath;
        } else {
            return SystemInfo.isMac ? this.myMacPath : this.myUnixPath;
        }
    }

    public String getName() {
        return this.myName;
    }

    public Icon getIcon() {
        return this.myIcon;
    }

    @Override
    public String toString() {
        return this.myName;
    }

    @Override
    public Icon getIcon(@IconFlags int flags) {
        return this.getIcon();
    }
}