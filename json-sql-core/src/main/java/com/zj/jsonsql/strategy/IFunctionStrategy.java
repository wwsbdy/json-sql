package com.zj.jsonsql.strategy;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.utils.SqlUtil;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author jie.zhou
 */
public interface IFunctionStrategy {

    Object get(Row row, SqlBasicCall sqlBasicCall);

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
        return SqlUtil.toString(sqlNode);
    }
}
