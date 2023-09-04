package com.zj.demoplugin.strategy;

import com.alibaba.fastjson.JSONObject;
import com.zj.demoplugin.strategy.impl.EqualsStrategy;
import com.zj.demoplugin.strategy.impl.InStrategy;
import com.zj.demoplugin.strategy.impl.RelationStrategy;
import org.apache.calcite.sql.SqlBasicCall;

import java.util.Objects;

/**
 * @author 19242
 */
public class StrategyBean {

    private static final AbstractStrategy ALWAYS_TURE_STRATEGY = new AbstractStrategy(false) {
        @Override
        public boolean apply(JSONObject item) {
            return true;
        }
    };

    private static final AbstractStrategy ALWAYS_FALSE_STRATEGY = new AbstractStrategy(false) {
        @Override
        public boolean apply(JSONObject item) {
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
            case OR:
            case AND:
                return new RelationStrategy(false, where.getKind(), where.getOperandList());
            default:
                return ALWAYS_FALSE_STRATEGY;
        }
    }

}
