package com.zj.jsonsql.strategy.impl;

import com.zj.jsonsql.entity.MyJson;
import com.zj.jsonsql.strategy.AbstractStrategy;
import com.zj.jsonsql.strategy.StrategyBean;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * and or
 * @author 19242
 */
public class RelationStrategy extends AbstractStrategy {

    SqlKind sqlKind;
    List<AbstractStrategy> strategyList;


    public RelationStrategy(SqlKind sqlKind, List<SqlNode> operandList) {
        super(false);
        if (SqlKind.AND != sqlKind && SqlKind.OR != sqlKind) {
            return;
        }
        this.sqlKind = sqlKind;
        if (CollectionUtils.isNotEmpty(operandList)) {
            strategyList = new ArrayList<>();
            for (SqlNode sqlNode : operandList) {
                strategyList.add(StrategyBean.getStrategy((SqlBasicCall) sqlNode));
            }
        }

    }

    @Override
    public boolean apply(MyJson item) {
        if (Objects.isNull(sqlKind) || CollectionUtils.isEmpty(strategyList)) {
            return false;
        }
        // or:至少有一个满足 and:全部满足
        boolean isOr = SqlKind.OR == sqlKind;
        for (AbstractStrategy strategy : strategyList) {
            if (isOr == strategy.apply(item)) {
                return isOr;
            }
        }
        return isOr;
    }
}
