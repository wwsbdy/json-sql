package com.zj.demoplugin.entity.table;

import com.intellij.ide.browsers.BrowserSpecificSettings;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.util.Function;
import com.intellij.util.PathUtil;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import com.intellij.util.ui.LocalPathCellEditor;
import com.intellij.util.ui.table.IconTableCellRenderer;
import com.intellij.util.ui.table.TableModelEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.util.Comparator;
import java.util.UUID;


/**
 * 参考
 *
 * @author arthur_zhou
 */
public class MyTable {

    private final TableModelEditor<MyRow> browsersEditor;

    private final JComponent browsersTable;

    public JComponent getBrowsersTable() {
        return browsersTable;
    }

    private static final FileChooserDescriptor APP_FILE_CHOOSER_DESCRIPTOR = FileChooserDescriptorFactory.createSingleFileOrExecutableAppDescriptor();


    private static final TableModelEditor.EditableColumnInfo<MyRow, Boolean> SELECT_OPT_COLUMN = new TableModelEditor.EditableColumnInfo<MyRow, Boolean>("select") {
        @Override
        public Class getColumnClass() {
            return Boolean.class;
        }

        @Override
        public Boolean valueOf(MyRow item) {
            return item.isActive();
        }

        @Override
        public void setValue(MyRow item, Boolean value) {
            item.setActive(value);
        }
    };

    private static final TableModelEditor.EditableColumnInfo<MyRow, String> NAME_COLUMN = new TableModelEditor.EditableColumnInfo<MyRow, String>("Name") {
        @Override
        public String valueOf(MyRow item) {
            return item.getName();
        }

        @Override
        public void setValue(MyRow item, String value) {
            item.setName(value);
        }

        /**
         * 添加排序比较,支持列排序
         */
        @NotNull
        @Override
        public Comparator<MyRow> getComparator() {
            return new Comparator<MyRow>() {
                @Override
                public int compare(MyRow o1, MyRow o2) {
                    if (o1.getName().compareTo(o2.getName()) > 0) {
                        return 1;
                    } else if (o1.getName().compareTo(o2.getName()) == 0) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            };
        }
    };

    private static final ColumnInfo<MyRow, MyIconItem> BROWSER_COLUMN = new ColumnInfo<MyRow, MyIconItem>("browser") {
        @Override
        public Class getColumnClass() {
            return MyIconItem.class;
        }

        @Override
        public MyIconItem valueOf(MyRow item) {
            return item.getIcon();
        }

        @Override
        public void setValue(MyRow item, MyIconItem value) {
            item.setIcon(value);
            item.setSpecificSettings(value.createBrowserSpecificSettings());
        }

        @NotNull
        @Override
        public TableCellRenderer getRenderer(MyRow item) {
            return IconTableCellRenderer.ICONABLE;
        }
    };

    private static final ColumnInfo[] COLUMNS = {
            SELECT_OPT_COLUMN,
            NAME_COLUMN,
            BROWSER_COLUMN,
            new TableModelEditor.EditableColumnInfo<MyRow, String>("Path") {
                @Override
                public String valueOf(MyRow item) {
                    return PathUtil.toSystemDependentName(item.getPath());
                }

                @Override
                public void setValue(MyRow item, String value) {
                    item.setPath(value);
                }

                @Nullable
                @Override
                public TableCellEditor getEditor(MyRow item) {
                    return new LocalPathCellEditor().fileChooserDescriptor(APP_FILE_CHOOSER_DESCRIPTOR).normalizePath(true);
                }
            }};


    public MyTable() {
        TableModelEditor.DialogItemEditor<MyRow> itemEditor = new TableModelEditor.DialogItemEditor<MyRow>() {
            @NotNull
            @Override
            public Class<MyRow> getItemClass() {
                return MyRow.class;
            }

            @Override
            public MyRow clone(@NotNull MyRow item, boolean forInPlaceEditing) {
                return new MyRow(forInPlaceEditing ? item.getId() : UUID.randomUUID(),
                        item.getIcon(), item.getName(), item.getPath(), item.isActive(),
                        forInPlaceEditing ? item.getSpecificSettings() : cloneSettings(item));
            }

            @Override
            public void edit(@NotNull MyRow browser, @NotNull Function<MyRow, MyRow> mutator, boolean isAdd) {
                BrowserSpecificSettings settings = cloneSettings(browser);
                if (settings != null && ShowSettingsUtil.getInstance().editConfigurable(browsersTable, settings.createConfigurable())) {
                    mutator.fun(browser).setSpecificSettings(settings);
                }
            }

            @Override
            public void applyEdited(@NotNull MyRow oldItem, @NotNull MyRow newItem) {
                oldItem.setSpecificSettings(newItem.getSpecificSettings());
            }

            @Override
            public boolean isEditable(@NotNull MyRow browser) {
                return false;
            }


            @Nullable
            private BrowserSpecificSettings cloneSettings(@NotNull MyRow browser) {
                BrowserSpecificSettings settings = browser.getSpecificSettings();
                if (settings == null) {
                    return null;
                }

                BrowserSpecificSettings newSettings = browser.getIcon().createBrowserSpecificSettings();
                assert newSettings != null;
                TableModelEditor.cloneUsingXmlSerialization(settings, newSettings);
                return newSettings;
            }

        };

        TableModelEditor.DataChangedListener<MyRow> dataChangedListener = new TableModelEditor.DataChangedListener<MyRow>() {
            @Override
            public void tableChanged(@NotNull TableModelEvent event) {
            }

            @Override
            public void dataChanged(@NotNull ColumnInfo<MyRow, ?> columnInfo, int rowIndex) {
            }
        };

        browsersEditor = new TableModelEditor<>(COLUMNS, itemEditor, "No web browsers configured");
        browsersEditor.modelListener(dataChangedListener);

        ListTableModel<MyRow> model = this.browsersEditor.getModel();
        MyRow myRow1 = new MyRow(
                UUID.randomUUID(),
                MyIconItem.CHROME,
                "3",
                "null",
                true,
                null
        );
        MyRow myRow2 = new MyRow(
                UUID.randomUUID(),
                MyIconItem.CHROME,
                "4",
                "null",
                true,
                null
        );

        model.addRow(myRow1);
        model.addRow(myRow2);

        browsersTable = browsersEditor.createComponent();
    }

}


