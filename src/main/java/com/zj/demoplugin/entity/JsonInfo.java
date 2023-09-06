package com.zj.demoplugin.entity;

import com.intellij.util.ui.ColumnInfo;
import com.zj.demoplugin.entity.columninfo.BooleanColumnInfo;
import com.zj.demoplugin.entity.columninfo.IdColumnInfo;
import com.zj.demoplugin.entity.columninfo.StrColumnInfo;
import com.zj.demoplugin.utils.SqlUtil;
import org.apache.calcite.sql.SqlSelect;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * json信息
 *
 * @author arthur_zhou
 */
public class JsonInfo extends BaseJsonInfo {
    /**
     * sql语句
     */
    private String sql;
    /**
     * sql解析树
     */
    private SqlSelect sqlNode;

    public JsonInfo(List<Field> columns, List<MyJson> list) {
        super(columns, list);
        resetSql();
    }

    public String getSql() {
        return sql;
    }

    public void resetSql() {
        this.sql = "select * from arr";
        sqlNode = null;
        setSqlNode();
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setSqlNode(SqlSelect sqlNode) {
        this.sqlNode = sqlNode;
    }

    @Override
    public ColumnInfo<?, ?>[] getFields() {
        List<Field> select = SqlUtil.getFields(super.getColumns(), sqlNode);
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

    private synchronized void setSqlNode() {
        if (Objects.isNull(sqlNode)) {
            sqlNode = SqlUtil.toSqlSelect(sql);
        }
        if (Objects.isNull(sqlNode)) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<MyJson> getRows() {
        List<MyJson> dataList = SqlUtil.getDataList(super.getList(), super.getColumns(), sqlNode);
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setId(i + 1);
        }
        return dataList;
    }
}
