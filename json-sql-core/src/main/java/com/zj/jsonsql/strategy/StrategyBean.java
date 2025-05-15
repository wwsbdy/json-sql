package com.zj.jsonsql.strategy;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.exception.SqlException;
import com.zj.jsonsql.strategy.impl.aggregate.*;
import com.zj.jsonsql.strategy.impl.compare.*;
import com.zj.jsonsql.strategy.impl.fourfundamentalrules.*;
import com.zj.jsonsql.strategy.impl.func.*;
import com.zj.jsonsql.ui.PluginBundle;
import com.zj.jsonsql.utils.CompareUtil;
import com.zj.jsonsql.utils.SqlUtil;
import org.apache.calcite.sql.*;

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
        public Object get(Row row, SqlBasicCall sqlBasicCall) {
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
            new IfStrategy(),
            new IfNullStrategy(),
            new LowerStrategy(),
            new NullIfStrategy(),
            new RoundStrategy(),
            new SubstringStrategy(),
            new SubstrStrategy(),
            new UpperStrategy(),
            new CountStrategy(),
            new IsNullStrategy(),
            new SumStrategy(),
            new MaxStrategy(),
            new MinStrategy(),
            new AvgStrategy(),
            new AnyValueStrategy(),
            new GroupArrayStrategy(),
            new GroupConcatStrategy(),
            new DivideStrategy(),
            new MinusStrategy(),
            new ModStrategy(),
            new PlusStrategy(),
            new TimesStrategy()
    ).collect(Collectors.toMap(IFunctionStrategy::getType, Function.identity(), (v1, v2) -> v2));

    /**
     * 获取数据过滤策略
     *
     * @param where where条件
     * @return 过滤策略
     */
    public static AbstractWhereStrategy getStrategy(SqlNode where) {
        if (Objects.isNull(where)) {
            return ALWAYS_TRUE_STRATEGY;
        }
        if (where instanceof SqlIdentifier) {
            return new AbstractWhereStrategy(false) {
                @Override
                public boolean apply(Row item) {
                    Object object = item.get(where);
                    return CompareUtil.isRight(object);
                }
            };
        }
        // 常量
        if (where instanceof SqlLiteral) {
            return new AbstractWhereStrategy(false) {
                @Override
                public boolean apply(Row item) {
                    Object object = SqlUtil.toString(where);
                    return CompareUtil.isRight(object);
                }
            };
        }
        SqlBasicCall sqlBasicCall = (SqlBasicCall) where;
        SqlKind kind = sqlBasicCall.getKind();
        switch (kind) {
            case EQUALS:
                return new EqualsStrategy(false, sqlBasicCall.getOperandList());
            case NOT_EQUALS:
                return new EqualsStrategy(true, sqlBasicCall.getOperandList());
            case IN:
                return new InStrategy(false, sqlBasicCall.getOperandList());
            case NOT_IN:
                return new InStrategy(true, sqlBasicCall.getOperandList());
            case LIKE:
                return new LikeStrategy(sqlBasicCall);
            case IS_NULL:
                return new NullStrategy(false, sqlBasicCall.getOperandList());
            case IS_NOT_NULL:
                return new NullStrategy(true, sqlBasicCall.getOperandList());
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUAL:
            case LESS_THAN:
            case LESS_THAN_OR_EQUAL:
            case BETWEEN:
                return new RangeStrategy(kind, sqlBasicCall.getOperandList());
            case OR:
            case AND:
                return new RelationStrategy(kind, sqlBasicCall.getOperandList());
            case PLUS:
            case MINUS:
            case TIMES:
            case DIVIDE:
            case MOD:
            case GROUP_CONCAT:
            case OTHER_FUNCTION:
                return new AbstractWhereStrategy(false) {
                    @Override
                    public boolean apply(Row item) {
                        Object flag = getFuncStrategy(sqlBasicCall.getOperator()).get(item, sqlBasicCall);
                        return CompareUtil.isRight(flag);
                    }
                };
            default:
                throw new SqlException(where + PluginBundle.get("error.message.grammar-not-supported"));
        }
    }

    public static IFunctionStrategy getFuncStrategy(SqlOperator operator) {
        return FUNCTION_STRATEGY_MAP.getOrDefault(FuncEnum.getByName(operator.getName()), EMPTY_FUNCTION_STRATEGY);
    }

}
