package com.zj.demoplugin.entity;

import com.alibaba.fastjson.JSONObject;
import com.intellij.util.ui.ColumnInfo;
import com.zj.demoplugin.utils.SqlUtil;
import org.apache.calcite.sql.SqlSelect;

import java.util.List;

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

    public JsonInfo(List<String> columns, List<JSONObject> list) {
        super(columns, list);
        this.sql = "select * from arr";
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public ColumnInfo<?, ?>[] getFields() {
        sqlNode = setSqlNode(sql);
        return SqlUtil.getFields(super.getColumns(), sqlNode);
    }

    private synchronized SqlSelect setSqlNode(String sql) {
        return SqlUtil.toSqlSelect(sql);
    }

    @Override
    public List<JSONObject> getRows() {
        return SqlUtil.getDataList(super.getList(), sqlNode);
    }
}
