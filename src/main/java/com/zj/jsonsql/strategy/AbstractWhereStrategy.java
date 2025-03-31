package com.zj.jsonsql.strategy;

import com.zj.jsonsql.entity.Row;
import org.apache.calcite.sql.SqlNode;

/**
 * where条件策略抽象类
 *
 * @author 19242
 */
public abstract class AbstractWhereStrategy {
    /**
     * 函数的operandList大小
     */
    protected static final int FUNC_SIZE = 1;
    /**
     * 普通判断的operandList大小
     */
    protected static final int SIMPLE_SIZE = 2;
    /**
     * between的operandList大小
     */
    protected static final int BETWEEN_SIZE = 3;
    /**
     * true,取反
     */
    private final boolean reverse;
    /**
     * 查询字段
     */
    private SqlNode field;


    public AbstractWhereStrategy(boolean reverse) {
        this.reverse = reverse;
    }

    protected boolean isReverse() {
        return reverse;
    }

    protected SqlNode getField() {
        return field;
    }

    protected void setField(SqlNode field) {
        this.field = field;
    }

    /**
     * 判断是否满足条件
     *
     * @param item 行值
     * @return 是否满足条件
     */
    public abstract boolean apply(Row item);
}
