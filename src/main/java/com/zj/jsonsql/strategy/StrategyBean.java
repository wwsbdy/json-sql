package com.zj.jsonsql.strategy;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.strategy.impl.*;
import com.zj.jsonsql.strategy.impl.func.*;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperator;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * where条件解析
 *
 * @author 19242
 */
public class StrategyBean {

    private static final AbstractWhereStrategy ALWAYS_TRUE_STRATEGY = new AbstractWhereStrategy(false) {
        @Override
        public boolean apply(Row item) {
            return true;
        }
    };

    private static final AbstractWhereStrategy ALWAYS_FALSE_STRATEGY = new AbstractWhereStrategy(false) {
        @Override
        public boolean apply(Row item) {
            return false;
        }
    };

    private static final IFunctionStrategy EMPTY_FUNCTION_STRATEGY = new IFunctionStrategy() {
        @Override
        public Object get(Row row, List<SqlNode> params) {
            return null;
        }

        @Override
        public FuncEnum getType() {
            return null;
        }
    };

    private static final Map<FuncEnum, IFunctionStrategy> FUNCTION_STRATEGY_MAP = Stream.of(
            new LeftStrategy(),
            new ConcatStrategy(),
            new LengthStrategy(),
            new RightStrategy(),
            new IfStrategy()
    ).collect(Collectors.toMap(IFunctionStrategy::getType, Function.identity(), (v1, v2) -> v2));

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
                return new LikeStrategy(where);
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

    public static IFunctionStrategy getFuncStrategy(SqlOperator operator) {
        return FUNCTION_STRATEGY_MAP.getOrDefault(FuncEnum.getByName(operator.getName()), EMPTY_FUNCTION_STRATEGY);
    }

}
