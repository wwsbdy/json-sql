package com.zj.jsonsql.strategy.impl;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.strategy.AbstractWhereStrategy;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 判空
 *
 * @author arthur_zhou
 */
public class NullStrategy extends AbstractWhereStrategy {


    public NullStrategy(boolean reverse, List<SqlNode> operandList) {
        super(reverse);
        if (CollectionUtils.isEmpty(operandList) || operandList.size() != FUNC_SIZE) {
            return;
        }
        setField(operandList.get(0));
    }

    @Override
    public boolean apply(Row item) {
        if (Objects.isNull(getField())) {
            return false;
        }
        return isReverse() != Objects.isNull(item.get(getField()));
    }
}
