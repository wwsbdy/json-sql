package com.zj.jsonsql.strategy.impl;

import com.zj.jsonsql.entity.MyJson;
import com.zj.jsonsql.strategy.AbstractWhereStrategy;
import com.zj.jsonsql.utils.CompareUtil;
import com.zj.jsonsql.utils.JsonUtil;
import lombok.Data;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 范围查询
 *
 * @author arthur_zhou
 */
public class RangeStrategy extends AbstractWhereStrategy {

    /**
     * 范围比较信息
     */
    private Range range;

    @Data
    private static class Range {
        private Object gt;
        private Object ge;
        private Object lt;
        private Object le;

        private boolean compare(Object var1) {
            if (Objects.nonNull(gt) && CompareUtil.compare(var1, gt) <= 0) {
                return false;
            }
            if (Objects.nonNull(ge) && CompareUtil.compare(var1, ge) < 0) {
                return false;
            }
            if (Objects.nonNull(lt) && CompareUtil.compare(var1, lt) >= 0) {
                return false;
            }
            return !Objects.nonNull(le) || CompareUtil.compare(var1, le) <= 0;
        }
    }


    public RangeStrategy(SqlKind sqlKind, List<SqlNode> operandList) {
        super(false);
        if (CollectionUtils.isEmpty(operandList) || operandList.size() < SIMPLE_SIZE) {
            return;
        }
        setField(operandList.get(0).toString());
        range = new Range();
        switch (sqlKind) {
            case GREATER_THAN:
                range.setGt(getValue(operandList.get(1)));
                break;
            case GREATER_THAN_OR_EQUAL:
                range.setGe(getValue(operandList.get(1)));
                break;
            case LESS_THAN:
                range.setLt(getValue(operandList.get(1)));
                break;
            case LESS_THAN_OR_EQUAL:
                range.setLe(getValue(operandList.get(1)));
                break;
            case BETWEEN:
                if (operandList.size() != BETWEEN_SIZE) {
                    break;
                }
                range.setGe(getValue(operandList.get(1)));
                range.setLe(getValue(operandList.get(2)));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean apply(MyJson item) {
        if (Objects.isNull(range)) {
            return false;
        }
        if (Objects.isNull(range.getGt()) && Objects.isNull(range.getGe()) && Objects.isNull(range.getLt()) && Objects.isNull(range.getLe())) {
            return false;
        }
        boolean equals = false;
        Object o = item.get(getField());
        Object convert = JsonUtil.convert(o);
        // 如果是数组，只要有一个满足就行
        if (convert instanceof List) {
            List<?> list = (List<?>) convert;
            for (Object o1 : list) {
                if (range.compare(o1)) {
                    equals = true;
                    break;
                }
            }
        } else {
            equals = range.compare(convert);
        }
        return equals;
    }
}
