package com.zj.jsonsql.strategy.impl.func;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.strategy.IFunctionStrategy;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.collections4.CollectionUtils;

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
        if (CollectionUtils.isEmpty(params)) {
            return null;
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
}
