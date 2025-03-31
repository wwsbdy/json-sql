package com.zj.jsonsql.strategy.impl.func;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.strategy.IFunctionStrategy;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * @author : jie.zhou
 * @date : 2025/3/31
 */
public class RoundStrategy implements IFunctionStrategy {
    @Override
    public Object get(Row row, List<SqlNode> params) {
        if (!isSupport(params)) {
            return null;
        }

        Object param1 = getValue(row, params.get(0));
        Object param2 = getValue(row, params.get(1));
        if (Objects.isNull(param1) || !(param1 instanceof BigDecimal) || Objects.isNull(param2) || !(param2 instanceof BigDecimal)) {
            return null;
        }
        return ((BigDecimal) param1).setScale(((BigDecimal) param2).intValue(), RoundingMode.HALF_UP);
    }

    @Override
    public FuncEnum getType() {
        return null;
    }

    @Override
    public boolean isSupport(List<SqlNode> params) {
        return !CollectionUtils.isEmpty(params) && params.size() == 2;
    }
}
