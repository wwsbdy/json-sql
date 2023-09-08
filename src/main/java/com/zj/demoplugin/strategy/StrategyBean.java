package com.zj.demoplugin.strategy;

import com.zj.demoplugin.entity.MyJson;
import com.zj.demoplugin.strategy.impl.*;
import org.apache.calcite.sql.SqlBasicCall;

import java.util.Objects;

/**
 * @author 19242
 */
public class StrategyBean {

    private static final AbstractStrategy ALWAYS_TURE_STRATEGY = new AbstractStrategy(false) {
        @Override
        public boolean apply(MyJson item) {
            return true;
        }
    };

    private static final AbstractStrategy ALWAYS_FALSE_STRATEGY = new AbstractStrategy(false) {
        @Override
        public boolean apply(MyJson item) {
            return false;
        }
    };

    public static AbstractStrategy getStrategy(SqlBasicCall where) {
        if (Objects.isNull(where)) {
            return ALWAYS_TURE_STRATEGY;
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
