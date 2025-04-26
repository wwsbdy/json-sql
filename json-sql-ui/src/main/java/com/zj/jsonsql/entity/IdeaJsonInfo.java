package com.zj.jsonsql.entity;

import com.intellij.util.ui.ColumnInfo;
import com.zj.jsonsql.entity.columninfo.BooleanColumnInfo;
import com.zj.jsonsql.entity.columninfo.IdColumnInfo;
import com.zj.jsonsql.entity.columninfo.StrColumnInfo;
import com.zj.jsonsql.entity.columninfo.TableColumnInfo;
import com.zj.jsonsql.ui.PluginBundle;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示的表格数据存放对象
 *
 * @author arthur_zhou
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class IdeaJsonInfo extends JsonInfo {

    public IdeaJsonInfo(List<Field> columns, List<Row> list, String jsonContent) {
        super(columns, list, jsonContent);
    }

    public IdeaJsonInfo() {
        super();
    }

    public IdeaJsonInfo(String jsonContent) {
        super(jsonContent);
    }
    public ColumnInfo<Row, ?>[] getFields() {
        List<Field> select = getSelect();
        List<TableColumnInfo<?>> columnInfos = new ArrayList<>();
        // 首位添加序号和选择框
        columnInfos.add(new IdColumnInfo());
        columnInfos.add(new BooleanColumnInfo(PluginBundle.get("table.select")));
        for (Field field : select) {
            columnInfos.add(new StrColumnInfo(field.getOriginalFiled(), field.getName(), field.getType()));
        }
        return columnInfos.toArray(new TableColumnInfo<?>[]{});
    }
}
