package com.zj.jsonsql.strategy;

import com.zj.jsonsql.entity.MyJson;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.util.NlsString;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author 19242
 */
public abstract class AbstractStrategy {
    /**
     * 函数的operandList大小
     */
    protected static final int FUNC_SIZE = 1;
    /**
     * 普通判断的operandList大小
     */
    protected static final int SIMPLE_SIZE = 2;
    /**
     *
     */
    protected static final int BETWEEN_SIZE = 3;
    /**
     * true,取反
     */
    boolean reverse;

    private String field;


    public AbstractStrategy(boolean reverse) {
        this.reverse = reverse;
    }

    protected boolean isReverse() {
        return reverse;
    }

    protected String getField() {
        return field;
    }

    protected void setField(String field) {
        this.field = field;
    }

    /**
     * 判断是否满足条件
     *
     * @param item 行值
     * @return 是否满足条件
     */
    public abstract boolean apply(MyJson item);

    /**
     * 获取查询值
     *
     * @param sqlNode sql解析树
     * @return 查询值
     */
    protected static Object getValue(SqlNode sqlNode) {
        if (Objects.isNull(sqlNode)) {
            return null;
        }
        if (sqlNode instanceof SqlIdentifier) {
            return ((SqlIdentifier) sqlNode).getSimple();
        }
        SqlLiteral sqlLiteral = (SqlLiteral) sqlNode;
        Object value = sqlLiteral.getValue();
        if (value instanceof NlsString) {
            value = ((NlsString) value).getValue().replaceAll("^'|'$", "");
        }
        if (value instanceof Number) {
            return new BigDecimal(String.valueOf(value));
        }
        return String.valueOf(value);
    }
}
