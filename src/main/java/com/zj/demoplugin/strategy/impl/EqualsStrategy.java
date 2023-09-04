package com.zj.demoplugin.strategy.impl;

import com.alibaba.fastjson.JSONObject;
import com.zj.demoplugin.strategy.AbstractStrategy;
import com.zj.demoplugin.utils.JsonUtil;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 等于
 *
 * @author 19242
 */
public class EqualsStrategy extends AbstractStrategy {

    private Object value;

    public EqualsStrategy(boolean reverse, List<SqlNode> operandList) {
        super(reverse);
        if (CollectionUtils.isEmpty(operandList) || operandList.size() != SIMPLE_SIZE) {
            return;
        }
        setField(operandList.get(0).toString());
        this.value = getValue(operandList.get(1));
    }

    @Override
    public boolean apply(JSONObject item) {
        if (Objects.isNull(item) || Objects.isNull(getField()) || Objects.isNull(value)) {
            return false;
        }
        Object o = JsonUtil.get(item, getField());
        boolean equals = value.equals(o);
        return isReverse() != equals;
    }
}
