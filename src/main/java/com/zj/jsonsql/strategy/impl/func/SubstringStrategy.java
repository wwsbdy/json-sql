package com.zj.jsonsql.strategy.impl.func;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.strategy.IFunctionStrategy;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author : jie.zhou
 * @date : 2025/3/31
 */
public class SubstringStrategy implements IFunctionStrategy {
    @Override
    public Object get(Row row, List<SqlNode> params) {
        if (!isSupport(params)) {
            return null;
        }
        Object param1 = getValue(row, params.get(0));
        Object param2 = getValue(row, params.get(1));
        Object param3 = getValue(row, params.get(2));
        if (Objects.isNull(param1)
                || Objects.isNull(param2) || !(param2 instanceof BigDecimal)
                || Objects.isNull(param3) || !(param3 instanceof BigDecimal)) {
            return null;
        }
        String str = param1.toString();
        int beginIndex = ((BigDecimal) param3).intValue();
        int endIndex = ((BigDecimal) param3).intValue();
        if (beginIndex < 0) {
            beginIndex = 0;
        }
        if (endIndex > str.length()) {
            endIndex = str.length();
        }
        if (beginIndex > endIndex) {
            return "";
        }
        return str.substring(beginIndex, endIndex);
    }

    @Override
    public FuncEnum getType() {
        return FuncEnum.SUBSTRING;
    }

    @Override
    public boolean isSupport(List<SqlNode> params) {
        return !CollectionUtils.isEmpty(params) && params.size() == 3;
    }
}
