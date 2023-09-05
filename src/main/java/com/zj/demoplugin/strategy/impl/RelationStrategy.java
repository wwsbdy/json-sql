package com.zj.demoplugin.strategy.impl;

import com.zj.demoplugin.entity.MyJson;
import com.zj.demoplugin.strategy.AbstractStrategy;
import com.zj.demoplugin.strategy.StrategyBean;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * and or
 * @author 19242
 */
public class RelationStrategy extends AbstractStrategy {

    SqlKind sqlKind;
    List<AbstractStrategy> strategyList;


    public RelationStrategy(boolean reverse, SqlKind sqlKind, List<SqlNode> operandList) {
        super(reverse);
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
        if (CollectionUtils.isEmpty(strategyList)) {
            return false;
        }
        for (AbstractStrategy strategy : strategyList) {
            boolean apply = strategy.apply(item);
            if (SqlKind.OR == sqlKind && apply) {
                return true;
            }
            if (SqlKind.AND == sqlKind && !apply) {
                return false;
            }
        }
        return isReverse() != (SqlKind.AND == sqlKind);
    }
}
