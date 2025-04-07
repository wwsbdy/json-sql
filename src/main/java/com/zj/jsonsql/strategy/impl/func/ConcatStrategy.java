package com.zj.jsonsql.strategy.impl.func;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.exception.SqlException;
import com.zj.jsonsql.strategy.IFunctionStrategy;
import org.apache.calcite.sql.SqlNode;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author : jie.zhou
 * @date : 2025/3/31
 */
public class ConcatStrategy implements IFunctionStrategy {
    @Override
    public Object get(Row row, List<SqlNode> params) {
        if (!isSupport(params)) {
            throw new SqlException(getType().name() + "函数参数错误");
        }
        return params.stream().map(param -> getValue(row, param))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    @Override
    public FuncEnum getType() {
        return FuncEnum.CONCAT;
    }

    @Override
    public boolean isSupport(List<SqlNode> params) {
        return IFunctionStrategy.super.isSupport(params);
    }
}
