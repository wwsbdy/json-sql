package com.zj.jsonsql.strategy.impl.fourfundamentalrules;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.exception.SqlException;
import com.zj.jsonsql.strategy.IFunctionStrategy;
import com.zj.jsonsql.ui.PluginBundle;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author : jie.zhou
 * @date : 2025/3/31
 */
public class ModStrategy implements IFunctionStrategy {


    @Override
    public Object get(Row row, SqlBasicCall sqlBasicCall) {
        List<SqlNode> params = sqlBasicCall.getOperandList();
        if (!isSupport(params)) {
            throw new SqlException("'%'" + PluginBundle.get("error.message.four-fundamental-rules-param-error"));
        }
        Object param1 = getValue(row, params.get(0));
        Object param2 = getValue(row, params.get(1));
        if (Objects.isNull(param1) || Objects.isNull(param2)) {
            return null;
        }
        String param1Str = param1.toString();
        String param2Str = param2.toString();
        if (NumberUtils.isCreatable(param1Str) && NumberUtils.isCreatable(param2Str)) {
            BigDecimal param2Num = new BigDecimal(param2Str);
            if (param2Num.compareTo(BigDecimal.ZERO) == 0) {
                return null;
            }
            return new BigDecimal(param1Str).remainder(param2Num);
        }
        throw new SqlException("'%'" + PluginBundle.get("error.message.four-fundamental-rules-param-error"));
    }

    @Override
    public FuncEnum getType() {
        return FuncEnum.MOD;
    }

    @Override
    public boolean isSupport(List<SqlNode> params) {
        return IFunctionStrategy.super.isSupport(params) && params.size() == 2;
    }
}
