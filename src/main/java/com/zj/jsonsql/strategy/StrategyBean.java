package com.zj.jsonsql.strategy;

import com.zj.jsonsql.entity.MyJson;
import com.zj.jsonsql.strategy.impl.*;
import org.apache.calcite.sql.SqlBasicCall;

import java.util.Objects;

/**
 * where条件解析
 *
 * @author 19242
 */
public class StrategyBean {

    private static final AbstractWhereStrategy ALWAYS_TRUE_STRATEGY = new AbstractWhereStrategy(false) {
        @Override
        public boolean apply(MyJson item) {
            return true;
        }
    };

    private static final AbstractWhereStrategy ALWAYS_FALSE_STRATEGY = new AbstractWhereStrategy(false) {
        @Override
        public boolean apply(MyJson item) {
            return false;
        }
    };

    /**
     * 获取数据过滤策略
     *
     * @param where where条件
     * @return 过滤策略
     */
    public static AbstractWhereStrategy getStrategy(SqlBasicCall where) {
        if (Objects.isNull(where)) {
            return ALWAYS_TRUE_STRATEGY;
        }
        switch (where.getKind()) {
            case EQUALS:
                return new EqualsStrategy(false, where.getOperandList());
            case NOT_EQUALS:
                return new EqualsStrategy(true, where.getOperandList());
            case IN:
                return new InStrategy(false, where.getOperandList());
            case NOT_IN:
                return new InStrategy(true, where.getOperandList());
            case LIKE:
                String operator = String.valueOf(where.getOperator());
                if (LikeStrategy.LIKE.equals(operator)) {
                    return new LikeStrategy(false, where.getOperandList());
                } else if (LikeStrategy.NOT_LIKE.equals(operator)) {
                    return new LikeStrategy(true, where.getOperandList());
                }
                return ALWAYS_FALSE_STRATEGY;
            case IS_NULL:
                return new NullStrategy(false, where.getOperandList());
            case IS_NOT_NULL:
                return new NullStrategy(true, where.getOperandList());
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUAL:
            case LESS_THAN:
            case LESS_THAN_OR_EQUAL:
            case BETWEEN:
                return new RangeStrategy(where.getKind(), where.getOperandList());
            case OR:
            case AND:
                return new RelationStrategy(where.getKind(), where.getOperandList());
            default:
                return ALWAYS_FALSE_STRATEGY;
        }
    }

}
