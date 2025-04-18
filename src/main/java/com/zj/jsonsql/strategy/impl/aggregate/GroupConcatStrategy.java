package com.zj.jsonsql.strategy.impl.aggregate;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.exception.SqlException;
import com.zj.jsonsql.strategy.IFunctionStrategy;
import com.zj.jsonsql.ui.PluginBundle;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelectKeyword;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : jie.zhou
 * @date : 2025/3/31
 */
public class GroupConcatStrategy implements IFunctionStrategy {
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
        Stream<String> stream = rows.stream().map(v -> getValue(v, param))
                .filter(Objects::nonNull)
                .map(Object::toString);
        SqlLiteral functionQuantifier = sqlBasicCall.getFunctionQuantifier();
        if (Objects.nonNull(functionQuantifier) && SqlSelectKeyword.DISTINCT.equals(functionQuantifier.getValue())) {
            stream = stream.distinct();
        }
        return stream.collect(Collectors.joining());
    }

    @Override
    public FuncEnum getType() {
        return FuncEnum.GROUP_CONCAT;
    }

    @Override
    public boolean isSupport(List<SqlNode> params) {
        return IFunctionStrategy.super.isSupport(params) && params.size() == 1;
    }
}
