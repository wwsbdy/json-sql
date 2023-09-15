package com.zj.jsonsql.entity;

import com.intellij.util.ui.ColumnInfo;
import com.zj.jsonsql.entity.columninfo.BooleanColumnInfo;
import com.zj.jsonsql.entity.columninfo.IdColumnInfo;
import com.zj.jsonsql.entity.columninfo.StrColumnInfo;
import com.zj.jsonsql.utils.SqlUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.calcite.sql.SqlSelect;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * json信息
 *
 * @author arthur_zhou
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class JsonInfo extends BaseJsonInfo {
    /**
     * sql语句
     */
    private String sql;
    /**
     * sql解析树
     */
    private SqlSelect sqlNode;
    /**
     * 查询列
     */
    private List<Field> select;
    /**
     * 查询结果
     */
    private List<MyJson> result;

    public JsonInfo(List<Field> columns, List<MyJson> list, String jsonContent) {
        super(columns, list, jsonContent);
        resetSql();
    }

    public void resetSql() {
        this.sql = "select * from arr";
        sqlNode = SqlUtil.toSqlSelect(sql);
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setSqlNode(SqlSelect sqlNode) {
        this.sqlNode = sqlNode;
    }

    @Override
    public ColumnInfo<?, ?>[] getFields() {
        select = SqlUtil.getFields(super.getColumns(), sqlNode);
        if (CollectionUtils.isEmpty(select)) {
            return new ColumnInfo[0];
        }
        ColumnInfo<?, ?>[] columnInfos = new ColumnInfo[select.size() + 2];
        // 首位添加序号和选择框
        columnInfos[0] = new IdColumnInfo();
        columnInfos[1] = new BooleanColumnInfo("选择");
        for (int i = 0; i < select.size(); i++) {
            Field field = select.get(i);
            columnInfos[i + 2] = new StrColumnInfo(field.getOriginalName(), field.getName(), field.getType());
        }
        return columnInfos;
    }

    @Override
    public List<MyJson> getRows() {
        return result = SqlUtil.getDataList(super.getList(), super.getColumns(), sqlNode);
    }
}
