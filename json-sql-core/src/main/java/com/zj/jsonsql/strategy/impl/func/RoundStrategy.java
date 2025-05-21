package com.zj.jsonsql.strategy.impl.func;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.strategy.IFunctionStrategy;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author : jie.zhou
 * @date : 2025/3/31
 */
public class RoundStrategy implements IFunctionStrategy {
    @Override
    public Object get(Row row, SqlBasicCall sqlBasicCall) {
        List<SqlNode> params = sqlBasicCall.getOperandList();
        if (!isSupport(params)) {
            return null;
        }
        Object param1 = getValue(row, params.get(0));
        Object param2 = getValue(row, params.get(1));
        if (NumberUtils.isCreatable(param1.toString()) && NumberUtils.isCreatable(param2.toString())) {
            return new BigDecimal(
                    new BigDecimal(param1.toString())
                            .setScale(new BigDecimal(param2.toString()).intValue(), RoundingMode.HALF_UP)
                            .stripTrailingZeros()
                            .toPlainString()
            );
        }
        return null;
    }

    @Override
    public FuncEnum getType() {
        return FuncEnum.ROUND;
    }

    @Override
    public boolean isSupport(List<SqlNode> params) {
        return IFunctionStrategy.super.isSupport(params) && params.size() == 2;
    }
}
