package com.zj.jsonsql.strategy.impl.aggregate;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.exception.SqlException;
import com.zj.jsonsql.strategy.IFunctionStrategy;
import com.zj.jsonsql.ui.PluginBundle;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author : jie.zhou
 * @date : 2025/3/31
 */
public class MaxStrategy implements IFunctionStrategy {


    @Override
    public Object get(Row row, SqlBasicCall sqlBasicCall) {
        List<SqlNode> params = sqlBasicCall.getOperandList();
        if (!isSupport(params)) {
            throw new SqlException(getType().name() + PluginBundle.get("error.message.func-param-error"));
        }
        List<Row> rows = row.getRows();
        if (CollectionUtils.isEmpty(rows)) {
            return null;
        }
        SqlNode param = params.get(0);
        Stream<Object> stream = rows.stream().map(v -> getValue(v, param)).filter(Objects::nonNull);
        // 全部是数字，用数字；是集合，用长度；否则用字符串
        if (rows.stream().map(v -> getValue(v, param)).filter(Objects::nonNull).allMatch(v -> v instanceof Number)) {
            return stream.map(v -> new BigDecimal(v.toString()))
                    .max(BigDecimal::compareTo)
                    .orElse(null);
        } else if (rows.stream().map(v -> getValue(v, param)).allMatch(v -> v instanceof Collection)) {
            return stream.map(v -> ((List<?>) v).size())
                    .max(Integer::compareTo)
                    .orElse(null);
        } else if (rows.stream().map(v -> getValue(v, param)).allMatch(v -> v instanceof Map)) {
            return stream.map(v -> ((Map<?, ?>) v).size())
                    .max(Integer::compareTo)
                    .orElse(null);
        }
        return stream.map(Object::toString)
                .max(String::compareTo)
                .orElse(null);
    }

    @Override
    public FuncEnum getType() {
        return FuncEnum.MAX;
    }

    @Override
    public boolean isSupport(List<SqlNode> params) {
        return IFunctionStrategy.super.isSupport(params) && params.size() == 1;
    }
}
