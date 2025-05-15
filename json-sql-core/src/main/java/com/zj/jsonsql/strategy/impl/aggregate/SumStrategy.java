package com.zj.jsonsql.strategy.impl.aggregate;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.strategy.IFunctionStrategy;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelectKeyword;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author : jie.zhou
 * @date : 2025/3/31
 */
public class SumStrategy implements IFunctionStrategy {


    @Override
    public Object get(Row row, SqlBasicCall sqlBasicCall) {
        List<SqlNode> params = sqlBasicCall.getOperandList();
        if (!isSupport(params)) {
            return null;
        }
        List<Row> rows = row.getRows();
        if (CollectionUtils.isEmpty(rows)) {
            return null;
        }
        SqlNode param = params.get(0);
        Stream<Object> stream = rows.stream().map(v -> getValue(v, param)).filter(Objects::nonNull);
        SqlLiteral functionQuantifier = sqlBasicCall.getFunctionQuantifier();
        if (Objects.nonNull(functionQuantifier) && SqlSelectKeyword.DISTINCT.equals(functionQuantifier.getValue())) {
            stream = stream.distinct();
        }
        // 要求全部是数字
        if (rows.stream().map(v -> getValue(v, param)).filter(Objects::nonNull).allMatch(v -> v instanceof Number)) {
            return stream.map(v -> new BigDecimal(v.toString())).reduce(BigDecimal::add).orElse(null);
        }
        return null;
    }

    @Override
    public FuncEnum getType() {
        return FuncEnum.SUM;
    }

    @Override
    public boolean isSupport(List<SqlNode> params) {
        return IFunctionStrategy.super.isSupport(params) && params.size() == 1;
    }
}
