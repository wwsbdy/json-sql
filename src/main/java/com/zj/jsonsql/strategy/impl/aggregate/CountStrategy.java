package com.zj.jsonsql.strategy.impl.aggregate;

import com.alibaba.fastjson.JSONObject;
import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.exception.SqlException;
import com.zj.jsonsql.strategy.IFunctionStrategy;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelectKeyword;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author : jie.zhou
 * @date : 2025/3/31
 */
public class CountStrategy implements IFunctionStrategy {


    @Override
    public Object get(Row row, SqlBasicCall sqlBasicCall) {
        List<SqlNode> params = sqlBasicCall.getOperandList();
        if (!isSupport(params)) {
            throw new SqlException(getType().name() + "函数参数错误");
        }
        List<Row> rows = row.getRows();
        if (CollectionUtils.isEmpty(rows)) {
            return 0;
        }
        Stream<JSONObject> stream = rows.stream().map(v -> {
            JSONObject jsonObject = new JSONObject();
            for (SqlNode param : params) {
                if ("*".equals(param.toString())) {
                    jsonObject.putAll(v.getJsonObject());
                    continue;
                }
                jsonObject.put(param.toString(), getValue(v, param));
            }
            return jsonObject;
        });
        SqlLiteral functionQuantifier = sqlBasicCall.getFunctionQuantifier();
        if (Objects.nonNull(functionQuantifier) && SqlSelectKeyword.DISTINCT.equals(functionQuantifier.getValue())) {
            stream = stream.distinct();
        }
        return stream.count();
    }

    @Override
    public FuncEnum getType() {
        return FuncEnum.COUNT;
    }
}
