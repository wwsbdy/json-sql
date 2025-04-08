package com.zj.jsonsql.strategy.impl.func;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.exception.SqlException;
import com.zj.jsonsql.strategy.IFunctionStrategy;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.lang3.math.NumberUtils;

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
    public Object get(Row row, SqlBasicCall sqlBasicCall) {
        List<SqlNode> params = sqlBasicCall.getOperandList();
        if (!isSupport(params)) {
            throw new SqlException(getType().name() + "函数参数错误");
        }
        Object param1 = getValue(row, params.get(0));
        Object param2 = getValue(row, params.get(1));
        if (Objects.isNull(param1) || !NumberUtils.isCreatable(param1.toString())
                || Objects.isNull(param2) || !NumberUtils.isCreatable(param2.toString())) {
            return null;
        }
        return new BigDecimal(param1.toString()).setScale(new BigDecimal(param2.toString()).intValue(), RoundingMode.HALF_UP);
    }

    @Override
    public FuncEnum getType() {
        return null;
    }

    @Override
    public boolean isSupport(List<SqlNode> params) {
        return IFunctionStrategy.super.isSupport(params) && params.size() == 2;
    }
}
