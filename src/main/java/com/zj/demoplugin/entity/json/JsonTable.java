package com.zj.demoplugin.entity.json;

import com.alibaba.fastjson.JSONObject;
import com.intellij.util.Function;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import com.intellij.util.ui.table.TableModelEditor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;


/**
 * 参考
 *
 * @author arthur_zhou
 */
@Data
public class JsonTable {

    private TableModelEditor<JSONObject> browsersEditor;

    private JComponent browsersTable;

    public JsonTable(Set<String> columnSet, Collection<JSONObject> jsonObjects) {
        ColumnInfo[] columns = new ColumnInfo[columnSet.size()];
        Object[] objects = columnSet.toArray();
        for (int i = 0; i < objects.length; i++) {
            String column = String.valueOf(objects[i]);
            columns[i] = new TableModelEditor.EditableColumnInfo<JSONObject, String>(column) {
                @Nullable
                @Override
                public String valueOf(JSONObject jsonObject) {
                    return jsonObject.getString(column);
                }
            };
        }

        TableModelEditor.DialogItemEditor<JSONObject> itemEditor = new TableModelEditor.DialogItemEditor<JSONObject>() {
            @Override
            public @NotNull Class<? extends JSONObject> getItemClass() {
                return JSONObject.class;
            }

            @Override
            public JSONObject clone(@NotNull JSONObject item, boolean forInPlaceEditing) {
                return null;
            }

            @Override
            public void edit(@NotNull JSONObject item, @NotNull Function<JSONObject, JSONObject> mutator, boolean isAdd) {

            }

            @Override
            public void applyEdited(@NotNull JSONObject oldItem, @NotNull JSONObject newItem) {

            }
        };

        TableModelEditor.DataChangedListener<JSONObject> dataChangedListener = new TableModelEditor.DataChangedListener<JSONObject>() {
            @Override
            public void tableChanged(@NotNull TableModelEvent event) {
            }

            @Override
            public void dataChanged(@NotNull ColumnInfo<JSONObject, ?> columnInfo, int rowIndex) {
            }
        };

        browsersEditor = new TableModelEditor<>(columns, itemEditor, "No web browsers configured");
        browsersEditor.modelListener(dataChangedListener);

        ListTableModel<JSONObject> model = this.browsersEditor.getModel();
        if (Objects.nonNull(jsonObjects)) {
            model.addRows(jsonObjects);
        }
        browsersTable = browsersEditor.createComponent();
    }

}


