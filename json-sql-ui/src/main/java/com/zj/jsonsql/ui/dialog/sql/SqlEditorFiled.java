package com.zj.jsonsql.ui.dialog.sql;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.ui.dialog.json.JsonEditorField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author : jie.zhou
 * @date : 2025/5/7
 */
public class SqlEditorFiled extends JsonEditorField {

    public static final Key<JsonInfo> JSON_INFO_KEY = Key.create("JSON_INFO_KEY");

    private final JsonInfo jsonInfo;

    public SqlEditorFiled(Language language, @Nullable Project project, JsonInfo jsonInfo) {
        super(language, project, jsonInfo.getSql());
        this.jsonInfo = jsonInfo;
    }

    @Override
    protected @NotNull EditorEx createEditor() {
        EditorEx editor = super.createEditor();
        editor.putUserData(JSON_INFO_KEY, jsonInfo);
        return editor;
    }
}
