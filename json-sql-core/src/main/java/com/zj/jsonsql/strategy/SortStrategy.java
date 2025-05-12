package com.zj.jsonsql.strategy;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.utils.CompareUtil;
import com.zj.jsonsql.utils.SqlUtil;
import lombok.Data;
import org.apache.calcite.sql.*;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 排序策略
 *
 * @author arthur_zhou
 */
public class SortStrategy {

    /**
     * 排序信息数组
     */
    private final List<Sort> sortList = new ArrayList<>();

    @Data
    private static class Sort {
        private SqlNode column;
        private boolean asc;

        public Sort(SqlNode column) {
            this(column, true);
        }

        public Sort(SqlNode column, boolean asc) {
            this.column = column;
            this.asc = asc;
        }
    }


    public SortStrategy(SqlNodeList orderList, Map<String, SqlNode> nameMap) {
        for (SqlNode sqlNode : orderList) {
            sqlNode = SqlUtil.replaceAlias(sqlNode, nameMap);
            if (sqlNode instanceof SqlIdentifier) {
                sortList.add(new Sort(sqlNode));
                continue;
            }
            if (sqlNode instanceof SqlBasicCall) {
                SqlBasicCall sqlBasicCall = (SqlBasicCall) sqlNode;
                if (sqlBasicCall.getOperator().isName("desc", false)) {
                    sortList.add(new Sort(sqlBasicCall.getOperandList().get(0), false));
                    continue;
                }
                sortList.add(new Sort(sqlNode));
            }
        }
    }

    /**
     * 排序
     *
     * @param var1 排序json1
     * @param var2 排序json2
     * @return 顺序倒序还是不变
     */
    public int orderBy(Row var1, Row var2) {
        if (CollectionUtils.isEmpty(sortList)) {
            return 0;
        }
        for (Sort sort : sortList) {
            Object o1 = get(var1, sort.getColumn());
            Object o2 = get(var2, sort.getColumn());
            int compare = CompareUtil.compare(o1, o2);
            if (compare == 0) {
                continue;
            }
            return sort.isAsc() ? compare : -compare;
        }
        return 0;
    }

    /**
     * 获取字段值
     *
     * @param var    Json
     * @param column key
     * @return value
     */
    Object get(Row var, SqlNode column) {
        if (Objects.isNull(var)) {
            return null;
        }
        return var.get(column);
    }
}
