package com.zj.jsonsql.strategy.impl;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.strategy.AbstractWhereStrategy;
import com.zj.jsonsql.utils.JsonUtil;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * in
 *
 * @author 19242
 */
public class InStrategy extends AbstractWhereStrategy {

    private Set<String> value;

    public InStrategy(boolean reverse, List<SqlNode> operandList) {
        super(reverse);
        if (CollectionUtils.isEmpty(operandList) || operandList.size() != SIMPLE_SIZE) {
            return;
        }
        setField(operandList.get(0));
        SqlNodeList param2 = (SqlNodeList) operandList.get(1);
        this.value = new HashSet<>();
        for (SqlNode sqlNode : param2) {
            Object value = getValue(sqlNode);
            if (Objects.nonNull(value)) {
                this.value.add(value.toString());
            }
        }
    }

    @Override
    public boolean apply(Row item) {
        if (Objects.isNull(item) || Objects.isNull(getField()) || Objects.isNull(value)) {
            return false;
        }
        boolean equals = false;
        Object o = item.get(getField());
        Object convert = JsonUtil.convert(o);
        // 如果是数组，只要有一个满足就行
        if (convert instanceof List) {
            List<?> list = (List<?>) convert;
            for (Object o1 : list) {
                if (value.contains(String.valueOf(o1))) {
                    equals = true;
                    break;
                }
            }
        } else {
            equals = value.contains(String.valueOf(convert));
        }
        return isReverse() != equals;
    }
}
