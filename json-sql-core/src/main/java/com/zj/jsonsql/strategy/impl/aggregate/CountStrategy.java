package com.zj.jsonsql.strategy.impl.aggregate;

import com.alibaba.fastjson.JSONObject;
import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.strategy.IFunctionStrategy;
import com.zj.jsonsql.utils.JsonUtil;
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
            return null;
        }
        List<Row> rows = row.getRows();
        if (CollectionUtils.isEmpty(rows)) {
            return 0;
        }
        // count(*)等效count(1)
        SqlLiteral functionQuantifier = sqlBasicCall.getFunctionQuantifier();
        if (params.size() == 1 && "*".equals(params.get(0).toString())) {
            if (Objects.nonNull(functionQuantifier) && SqlSelectKeyword.DISTINCT.equals(functionQuantifier.getValue())) {
                return 1;
            }
            return rows.size();
        }
        return getCount(rows, params, functionQuantifier);
    }

    private long getCount(List<Row> rows, List<SqlNode> params, SqlLiteral functionQuantifier) {
        Stream<?> stream = rows.stream().map(v -> {
            JSONObject jsonObject = new JSONObject();
            for (SqlNode param : params) {
                // 如果是count(*, a)，忽略*，如果count(a,b)，a或b为null的行不参与计算
                if ("*".equals(param.toString())) {
                    continue;
                }
                Object value = getValue(v, param);
                if (Objects.isNull(value)) {
                    return JsonUtil.EMPTY_JSON_OBJECT;
                }
                jsonObject.put(param.toString(), value);
            }
            return jsonObject;
        }).filter(v -> !v.isEmpty());
        if (Objects.nonNull(functionQuantifier) && SqlSelectKeyword.DISTINCT.equals(functionQuantifier.getValue())) {
            stream = stream.distinct();
        }
        return stream.count();
    }

    @Override
    public FuncEnum getType() {
        return FuncEnum.COUNT;
    }

    @Override
    public boolean isSupport(List<SqlNode> params) {
        return CollectionUtils.isNotEmpty(params);
    }
}
