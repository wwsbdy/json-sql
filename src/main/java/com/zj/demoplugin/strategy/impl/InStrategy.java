package com.zj.demoplugin.strategy.impl;

import com.alibaba.fastjson.JSONObject;
import com.zj.demoplugin.strategy.AbstractStrategy;
import com.zj.demoplugin.utils.JsonUtil;
import org.apache.calcite.sql.SqlLiteral;
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
public class InStrategy extends AbstractStrategy {

    private Set<Object> value;

    public InStrategy(boolean reverse, List<SqlNode> operandList) {
        super(reverse);
        if (CollectionUtils.isEmpty(operandList) || operandList.size() != SIMPLE_SIZE) {
            return;
        }
        setField(operandList.get(0).toString());
        SqlNodeList param2 = (SqlNodeList) operandList.get(1);
        this.value = new HashSet<>();
        for (SqlNode sqlNode : param2) {
            Object value = getValue((SqlLiteral) sqlNode);
            if (Objects.nonNull(value)) {
                this.value.add(value);
            }
        }
    }

    @Override
    public boolean apply(JSONObject item) {
        if (Objects.isNull(item) || Objects.isNull(getField()) || Objects.isNull(value)) {
            return false;
        }
        Object o = JsonUtil.get(item, getField());
        boolean equals = value.contains(o);
        return isReverse() != equals;
    }
}
