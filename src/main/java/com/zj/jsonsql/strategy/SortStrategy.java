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
 * 排序策略
 *
 * @author arthur_zhou
 */
public class SortStrategy {

    /**
     * 排序信息数组
     */
    private final List<Sort> sortList = new ArrayList<>();
    /**
     * 别名和真实名称map
     */
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
                continue;
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
     * @param var1 排序json1
     * @param var2 排序json2
     * @return 顺序倒序还是不变
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
     * @param var    Json
     * @param column key
     * @return value
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
        String join = String.join(".", split);
        return var.get(join);
    }
}
