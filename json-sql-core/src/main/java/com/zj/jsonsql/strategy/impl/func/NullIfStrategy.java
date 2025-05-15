package com.zj.jsonsql.strategy.impl.func;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.strategy.IFunctionStrategy;
import com.zj.jsonsql.utils.CompareUtil;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlNode;

import java.util.List;

/**
 * @author : jie.zhou
 * @date : 2025/3/31
 */
public class NullIfStrategy implements IFunctionStrategy {
    @Override
    public Object get(Row row, SqlBasicCall sqlBasicCall) {
        List<SqlNode> params = sqlBasicCall.getOperandList();
        if (!isSupport(params)) {
            return null;
        }
        Object param1 = getValue(row, params.get(0));
        Object param2 = getValue(row, params.get(1));
        return 0 == CompareUtil.compare(param1, param2) ? null : param1;
    }

    @Override
    public FuncEnum getType() {
        return FuncEnum.NULLIF;
    }

    @Override
    public boolean isSupport(List<SqlNode> params) {
        return IFunctionStrategy.super.isSupport(params) && params.size() == 2;
    }
}
