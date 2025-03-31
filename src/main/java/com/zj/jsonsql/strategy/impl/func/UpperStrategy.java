package com.zj.jsonsql.strategy.impl.func;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.strategy.IFunctionStrategy;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author : jie.zhou
 * @date : 2025/3/31
 */
public class UpperStrategy implements IFunctionStrategy {
    @Override
    public Object get(Row row, List<SqlNode> params) {
        if (!isSupport(params)) {
            return null;
        }
        Object value = getValue(row, params.get(0));
        return Objects.nonNull(value) ? value.toString().toUpperCase() : null;
    }

    @Override
    public FuncEnum getType() {
        return FuncEnum.UPPER;
    }

    @Override
    public boolean isSupport(List<SqlNode> params) {
        return !CollectionUtils.isEmpty(params) && params.size() == 1;
    }
}
