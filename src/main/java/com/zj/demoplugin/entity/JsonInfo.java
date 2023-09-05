package com.zj.demoplugin.entity;

import com.intellij.openapi.ui.Messages;
import com.intellij.util.ui.ColumnInfo;
import com.zj.demoplugin.enums.NoticeEnum;
import com.zj.demoplugin.utils.SqlUtil;
import org.apache.calcite.sql.SqlSelect;

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
        this.sql = "select * from arr";
    }

    public String getSql() {
        return sql;
    }

    public void resetSql(String sql) {
        this.sql = sql;
        sqlNode = null;
        setSqlNode(sql);
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setSqlNode(SqlSelect sqlNode) {
        this.sqlNode = sqlNode;
    }

    @Override
    public ColumnInfo<?, ?>[] getFields() {
        setSqlNode(sql);
        return SqlUtil.getFields(super.getColumns(), sqlNode);
    }

    private synchronized void setSqlNode(String sql) {
        if (Objects.isNull(sqlNode)) {
            sqlNode = SqlUtil.toSqlSelect(sql);
        }
        if (Objects.isNull(sqlNode)) {
            Messages.showErrorDialog(NoticeEnum.SQL_ERROR.getMessage(), NoticeEnum.SQL_ERROR.getWarn());
            throw new RuntimeException();
        }
    }

    @Override
    public List<MyJson> getRows() {
        setSqlNode(sql);
        return SqlUtil.getDataList(super.getList(), sqlNode);
    }
}
