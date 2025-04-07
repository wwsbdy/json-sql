package com.zj.jsonsql.entity;

import com.intellij.util.ui.ColumnInfo;
import com.zj.jsonsql.entity.columninfo.BooleanColumnInfo;
import com.zj.jsonsql.entity.columninfo.IdColumnInfo;
import com.zj.jsonsql.entity.columninfo.StrColumnInfo;
import com.zj.jsonsql.utils.SqlUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 展示的表格数据存放对象
 *
 * @author arthur_zhou
 */
@Getter
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class JsonInfo extends BaseJsonInfo {
    /**
     * sql语句
     */
    @Setter
    private String sql;
    /**
     * sql解析树
     */
    @Setter
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

    public void resetSql() {
        this.sql = "select * from arr";
        try {
            sqlNode = SqlUtil.toSqlSelect(sql);
        } catch (SqlParseException e) {
            log.error("SqlParseException：", e);
        }
    }

    @Override
    public ColumnInfo<Row, ?>[] getFields() {
        if (Objects.isNull(select)) {
            select = SqlUtil.getFields(super.getColumns(), sqlNode);
        }
        if (CollectionUtils.isEmpty(select)) {
            return new ColumnInfo[0];
        }
        ColumnInfo<Row, ?>[] columnInfos = new ColumnInfo[select.size() + 2];
        // 首位添加序号和选择框
        columnInfos[0] = new IdColumnInfo();
        columnInfos[1] = new BooleanColumnInfo("选择");
        for (int i = 0; i < select.size(); i++) {
            Field field = select.get(i);
            columnInfos[i + 2] = new StrColumnInfo(field.getOriginalFiled(), field.getName(), field.getType());
        }
        return columnInfos;
    }

    @Override
    public List<Row> getRows() {
        return result = SqlUtil.getDataList(super.getList(), super.getColumns(), sqlNode);
    }
}
