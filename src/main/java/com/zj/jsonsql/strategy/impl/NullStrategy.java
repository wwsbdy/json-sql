package com.zj.jsonsql.strategy.impl;

import com.zj.jsonsql.entity.MyJson;
import com.zj.jsonsql.strategy.AbstractStrategy;
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
public class NullStrategy extends AbstractStrategy {


    public NullStrategy(boolean reverse, List<SqlNode> operandList) {
        super(reverse);
        if (CollectionUtils.isEmpty(operandList) || operandList.size() != FUNC_SIZE) {
            return;
        }
        setField(operandList.get(0).toString());
    }

    @Override
    public boolean apply(MyJson item) {
        if (StringUtils.isEmpty(getField())) {
            return false;
        }
        return isReverse() != Objects.isNull(item.get(getField()));
    }
}
