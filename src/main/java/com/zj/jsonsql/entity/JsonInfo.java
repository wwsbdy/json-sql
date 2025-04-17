package com.zj.jsonsql.entity;

import com.intellij.util.ui.ColumnInfo;
import com.zj.jsonsql.entity.columninfo.BooleanColumnInfo;
import com.zj.jsonsql.entity.columninfo.IdColumnInfo;
import com.zj.jsonsql.entity.columninfo.StrColumnInfo;
import com.zj.jsonsql.entity.columninfo.TableColumnInfo;
import com.zj.jsonsql.utils.SqlUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
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
@Slf4j
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
        } catch (SqlParseException e) {
            log.error("SqlParseException：", e);
        }
        select = null;
        result = null;
    }

    @Override
    public ColumnInfo<Row, ?>[] getFields() {
        if (Objects.isNull(select)) {
            select = SqlUtil.getFields(super.getColumns(), sqlNode);
        }
        if (CollectionUtils.isEmpty(select)) {
            select = Collections.emptyList();
        }
        List<TableColumnInfo<?>> columnInfos = new ArrayList<>();
        // 首位添加序号和选择框
        columnInfos.add(new IdColumnInfo());
        columnInfos.add(new BooleanColumnInfo("选择"));
        for (Field field : select) {
            columnInfos.add(new StrColumnInfo(field.getOriginalFiled(), field.getName(), field.getType()));
        }
        return columnInfos.toArray(new TableColumnInfo<?>[]{});
    }

    @Override
    public List<Row> getRows() {
        if (Objects.nonNull(result)) {
            return result;
        }
        return result = SqlUtil.getDataList(super.getList(), super.getColumns(), sqlNode);
    }
}
