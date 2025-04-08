package com.zj.jsonsql.strategy.impl.func;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.exception.SqlException;
import com.zj.jsonsql.strategy.IFunctionStrategy;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlNode;

import java.util.List;
import java.util.Objects;

/**
 * @author : jie.zhou
 * @date : 2025/3/31
 */
public class LengthStrategy implements IFunctionStrategy {
    @Override
    public Object get(Row row, SqlBasicCall sqlBasicCall) {
        List<SqlNode> params = sqlBasicCall.getOperandList();
        if (!isSupport(params)) {
            throw new SqlException(getType().name() + "函数参数错误");
        }
        Object value = getValue(row, params.get(0));
        if (Objects.isNull(value)) {
            return null;
        }
        return value.toString().length();
    }

    @Override
    public FuncEnum getType() {
        return FuncEnum.LENGTH;
    }

    @Override
    public boolean isSupport(List<SqlNode> params) {
        return IFunctionStrategy.super.isSupport(params) && params.size() == 1;
    }
}
