package com.zj.demoplugin.strategy;

import com.alibaba.fastjson.JSONObject;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.util.NlsString;

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
     * ture,取反
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
     * 判断
     *
     * @param item 行值
     * @return
     */
    public abstract boolean apply(JSONObject item);

    protected static Object getValue(SqlLiteral sqlLiteral) {
        if (Objects.isNull(sqlLiteral)) {
            return null;
        }
        Object value = sqlLiteral.getValue();
        if (Objects.isNull(value)) {
            return null;
        }
        if (value instanceof NlsString) {
            value = ((NlsString) value).getValue().replaceAll("^'|'$", "");
        }
        return value;
    }
}
