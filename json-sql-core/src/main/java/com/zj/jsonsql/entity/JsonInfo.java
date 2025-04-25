package com.zj.jsonsql.entity;

import com.zj.jsonsql.utils.SqlUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParseException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 展示的表格数据存放对象
 *
 * @author arthur_zhou
 */
@Getter
@Setter
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
    private List<Row> result;

    public JsonInfo(List<Field> columns, List<Row> list, String jsonContent) {
        super(columns, list, jsonContent);
        resetSql();
    }

    public JsonInfo() {
        super(Collections.emptyList(), Collections.emptyList(), "");
        resetSql();
    }

    public JsonInfo(String jsonContent) {
        super(Collections.emptyList(), Collections.emptyList(), jsonContent);
        resetSql();
    }

    public void resetSql() {
        this.sql = "select * from arr";
        try {
            sqlNode = SqlUtil.toSqlSelect(sql);
        } catch (SqlParseException ignored) {
        }
        select = null;
        result = null;
    }

    @Override
    public List<Row> getRows() {
        if (Objects.nonNull(result)) {
            return result;
        }
        return result = SqlUtil.getDataList(super.getList(), super.getColumns(), sqlNode);
    }
}
