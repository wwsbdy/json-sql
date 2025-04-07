package com.zj.jsonsql.strategy;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.util.NlsString;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author jie.zhou
 */
public interface IFunctionStrategy {

    Object get(Row row, List<SqlNode> params);

    default Object get(Row row, SqlBasicCall sqlBasicCall) {
        return get(row, sqlBasicCall.getOperandList());
    }

    FuncEnum getType();

    default boolean isSupport(List<SqlNode> params) {
        return CollectionUtils.isNotEmpty(params);
    }

    default Object getValue(Row row, SqlNode sqlNode) {
        if (Objects.isNull(sqlNode)) {
            return null;
        }
        if (sqlNode instanceof SqlIdentifier || sqlNode instanceof SqlBasicCall) {
            return row.get(sqlNode);
        }
        SqlLiteral sqlLiteral = (SqlLiteral) sqlNode;
        Object value = sqlLiteral.getValue();
        if (value instanceof NlsString) {
            value = ((NlsString) value).getValue().replaceAll("^'|'$", "");
        }
        if (value instanceof Number) {
            return new BigDecimal(String.valueOf(value));
        }
        return String.valueOf(value);
    }
}
