package com.zj.jsonsql.strategy.impl.compare;

import com.zj.jsonsql.entity.Row;
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
        private SqlNode gt;
        private SqlNode ge;
        private SqlNode lt;
        private SqlNode le;

        private boolean compare(Row item, Object var1) {
            if (Objects.nonNull(gt) && CompareUtil.compare(var1, item.get(gt)) <= 0) {
                return false;
            }
            if (Objects.nonNull(ge) && CompareUtil.compare(var1, item.get(ge)) < 0) {
                return false;
            }
            if (Objects.nonNull(lt) && CompareUtil.compare(var1, item.get(lt)) >= 0) {
                return false;
            }
            return Objects.isNull(le) || CompareUtil.compare(var1, item.get(le)) <= 0;
        }
    }


    public RangeStrategy(SqlKind sqlKind, List<SqlNode> operandList) {
        super(false);
        if (CollectionUtils.isEmpty(operandList) || operandList.size() < SIMPLE_SIZE) {
            return;
        }
        setField(operandList.get(0));
        range = new Range();
        switch (sqlKind) {
            case GREATER_THAN:
                range.setGt(operandList.get(1));
                break;
            case GREATER_THAN_OR_EQUAL:
                range.setGe(operandList.get(1));
                break;
            case LESS_THAN:
                range.setLt(operandList.get(1));
                break;
            case LESS_THAN_OR_EQUAL:
                range.setLe(operandList.get(1));
                break;
            case BETWEEN:
                if (operandList.size() != BETWEEN_SIZE) {
                    break;
                }
                range.setGe(operandList.get(1));
                range.setLe(operandList.get(2));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean apply(Row item) {
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
                if (range.compare(item, o1)) {
                    equals = true;
                    break;
                }
            }
        } else {
            equals = range.compare(item, convert);
        }
        return equals;
    }
}
