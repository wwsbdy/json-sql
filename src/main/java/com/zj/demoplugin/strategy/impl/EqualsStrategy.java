package com.zj.demoplugin.strategy.impl;

import com.zj.demoplugin.entity.MyJson;
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

    private String value;

    public EqualsStrategy(boolean reverse, List<SqlNode> operandList) {
        super(reverse);
        if (CollectionUtils.isEmpty(operandList) || operandList.size() != SIMPLE_SIZE) {
            return;
        }
        setField(operandList.get(0).toString());
        this.value = String.valueOf(getValue(operandList.get(1)));
    }

    @Override
    public boolean apply(MyJson item) {
        if (Objects.isNull(item) || Objects.isNull(getField()) || Objects.isNull(value)) {
            return false;
        }
        boolean equals = false;
        Object o = item.get(getField());
        Object convert = JsonUtil.convert(o);
        // 如果是数组，只要有一个满足就行
        if (convert instanceof List) {
            List list = (List) convert;
            for (Object o1 : list) {
                if (value.equals(String.valueOf(o1))) {
                    equals = true;
                    break;
                }
            }
        } else {
            equals = value.equals(String.valueOf(convert));
        }
        return isReverse() != equals;
    }
}
