package com.zj.jsonsql.strategy;

import com.zj.jsonsql.entity.MyJson;
import com.zj.jsonsql.utils.CompareUtil;
import lombok.Data;
import org.apache.calcite.sql.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author arthur_zhou
 */
public class SortStrategy {


    private final List<Sort> sortList = new ArrayList<>();
    private final Map<String, String> nameMap;

    @Data
    private static class Sort {
        private String column;
        private boolean asc;

        public Sort(String column) {
            this(column, true);
        }

        public Sort(String column, boolean asc) {
            this.column = column;
            this.asc = asc;
        }
    }


    public SortStrategy(SqlNodeList orderList, Map<String, String> nameMap) {
        this.nameMap = nameMap;
        for (SqlNode sqlNode : orderList) {
            if (sqlNode instanceof SqlIdentifier) {
                SqlIdentifier sqlIdentifier = (SqlIdentifier) sqlNode;
                sortList.add(new Sort(sqlIdentifier.toString()));
            }
            if (sqlNode instanceof SqlBasicCall) {
                SqlBasicCall sqlBasicCall = (SqlBasicCall) sqlNode;
                if (sqlBasicCall.getOperator().isName("desc", false)) {
                    sortList.add(new Sort(sqlBasicCall.getOperandList().get(0).toString(), false));
                }
            }
        }
    }

    /**
     * 排序
     *
     * @param var1
     * @param var2
     * @return
     */
    public int orderBy(MyJson var1, MyJson var2) {
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
     * @param var
     * @param column
     * @return
     */
    Object get(MyJson var, String column) {
        if (Objects.isNull(var) || StringUtils.isEmpty(column)) {
            return null;
        }
        String[] split = column.split("\\.");
        if (split.length == 0) {
            return null;
        }
        String realColumn = nameMap.get(split[0]);
        if (StringUtils.isEmpty(realColumn)) {
            return null;
        }
        split[0] = realColumn;
        return var.get(split);
    }
}
