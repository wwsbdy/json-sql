package com.zj.jsonsql.strategy.impl.compare;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.strategy.AbstractWhereStrategy;
import com.zj.jsonsql.utils.JsonUtil;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 模糊查询
 *
 * @author arthur_zhou
 */
public class LikeStrategy extends AbstractWhereStrategy {

    private SqlNode value;

    public static final String LIKE = "LIKE";
    public static final String NOT_LIKE = "NOT LIKE";

    public LikeStrategy(boolean reverse, List<SqlNode> operandList) {
        super(reverse);
        if (CollectionUtils.isEmpty(operandList) || operandList.size() != SIMPLE_SIZE) {
            return;
        }
        setField(operandList.get(0));
        this.value = operandList.get(1);
    }

    public LikeStrategy(SqlBasicCall where) {
        this(LikeStrategy.NOT_LIKE.equalsIgnoreCase(String.valueOf(where.getOperator())), where.getOperandList());
    }

    @Override
    public boolean apply(Row item) {
        if (Objects.isNull(item) || Objects.isNull(getField()) || Objects.isNull(value)) {
            return false;
        }
        boolean equals = false;
        Object o = item.get(getField());
        Object convert = JsonUtil.convert(o);
        String compareValue = String.valueOf(item.get(value))
                .replaceAll("(?<!\\\\)_", ".")
                .replaceAll("(?<!\\\\)%", ".*")
                // \\%或\\_变成%或_
                .replaceAll("\\\\\\\\(?=[_%])", "");
        // 如果是数组，只要有一个满足就行
        if (convert instanceof List) {
            List<?> list = (List<?>) convert;
            for (Object o1 : list) {
                if (String.valueOf(o1).matches(compareValue)) {
                    equals = true;
                    break;
                }
            }
        } else {
            equals = String.valueOf(convert).matches(compareValue);
        }
        return isReverse() != equals;
    }
}
