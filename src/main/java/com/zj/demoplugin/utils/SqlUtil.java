package com.zj.demoplugin.utils;

import com.zj.demoplugin.entity.Field;
import com.zj.demoplugin.entity.MyJson;
import com.zj.demoplugin.strategy.AbstractStrategy;
import com.zj.demoplugin.strategy.SortStrategy;
import com.zj.demoplugin.strategy.StrategyBean;
import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author arthur_zhou
 */
public class SqlUtil {

    private static final SqlParser.Config CONFIG;

    static {
        CONFIG = SqlParser.config()
                .withLex(Lex.MYSQL)
                .withConformance(SqlConformanceEnum.MYSQL_5)
                // 保持原有大小写
                .withCaseSensitive(false);
    }

    /**
     * 获取满足条件的数据
     *
     * @param dataList
     * @param columns
     * @param sqlNode
     * @return
     */
    public static List<MyJson> getDataList(List<MyJson> dataList, List<Field> columns, SqlSelect sqlNode) {
        Objects.requireNonNull(sqlNode);
        if (CollectionUtils.isEmpty(dataList)) {
            return Collections.emptyList();
        }
        SqlNode where = sqlNode.getWhere();
        AbstractStrategy strategy = StrategyBean.getStrategy((SqlBasicCall) where);
        Stream<MyJson> stream = dataList.stream().filter(strategy::apply);
        SqlNodeList orderList = sqlNode.getOrderList();
        if (CollectionUtils.isNotEmpty(orderList)) {
            // 获取查询的字段
            List<Field> selectList = getSelectList(columns, sqlNode);
            // 追加表字段
            selectList.addAll(columns);
            // 获取别名和原始名
            Map<String, String> nameMap = selectList.stream()
                    .filter(v->StringUtils.isNotEmpty(v.getOriginalName()))
                    .collect(Collectors.toMap(v-> StringUtils.isEmpty(v.getName()) ? v.getOriginalName() : v.getName(), Field::getOriginalName, (v1, v2) -> v2));
            SortStrategy sortStrategy = new SortStrategy(orderList, nameMap);
            stream = stream.sorted(sortStrategy::orderBy);
        }
        SqlNode offset = sqlNode.getOffset();
        if (Objects.nonNull(offset)) {
            stream = stream.skip(Long.parseLong(offset.toString()));
        }
        SqlNode fetch = sqlNode.getFetch();
        if (Objects.nonNull(fetch)) {
            stream = stream.limit(Long.parseLong(fetch.toString()));
        }
        return stream.collect(Collectors.toList());
    }

    /**
     * 获取字段名
     *
     * @param columns
     * @param sqlNode
     * @return
     */
    public static List<Field> getFields(List<Field> columns, SqlSelect sqlNode) {
        Objects.requireNonNull(sqlNode);
        if (CollectionUtils.isEmpty(columns)) {
            return Collections.emptyList();
        }
        List<Field> select = getSelectList(columns, sqlNode);
        Map<String, String> typeMap = columns.stream().collect(Collectors.toMap(Field::getOriginalName, Field::getType, (v1, v2) -> v2));
        // 取交集
        select.removeIf(v -> {
            if (StringUtils.isEmpty(v.getOriginalName())) {
                return true;
            }
            String[] keys = v.getOriginalName().split("\\.");
            if (keys.length == 0) {
                return true;
            }
            String type = typeMap.get(keys[0]);
            if (StringUtils.isEmpty(type)) {
                return true;
            }
            v.setType(type);
            return false;
        });
        return select;
    }

    /**
     * 获取查询字段
     * @param columns
     * @param sqlNode
     * @return
     */
    @NotNull
    public static List<Field> getSelectList(List<Field> columns, SqlSelect sqlNode) {
        List<Field> select = new ArrayList<>();
        for (SqlNode node : sqlNode.getSelectList()) {
            String name = node.toString();
            // 查全部
            if ("*".equals(name)) {
                for (Field field : columns) {
                    String column = field.getOriginalName();
                    select.add(new Field(column, column));
                }
                continue;
            }
            switch (node.getKind()) {
                case IDENTIFIER:
                    select.add(new Field(name, name));
                    break;
                case AS:
                    List<SqlNode> operandList = ((SqlBasicCall) node).getOperandList();
                    if (CollectionUtils.isNotEmpty(operandList) && operandList.size() == 2) {
                        select.add(new Field(String.valueOf(operandList.get(0)), String.valueOf(operandList.get(1))));
                    }
                    break;
                default:
                    break;
            }
        }
        return select;
    }

    /**
     * 解析sql
     *
     * @param sql
     * @return
     */
    public static SqlSelect toSqlSelect(String sql) {
        if (StringUtils.isEmpty(sql)) {
            return null;
        }
        try {
            SqlParser parser = SqlParser.create(sql, CONFIG);
            SqlNode node = parser.parseStmt();
            SqlKind kind = node.getKind();
            if (SqlKind.SELECT == kind) {
                return (SqlSelect) node;
            }
            if (SqlKind.ORDER_BY == kind) {
                SqlOrderBy orderBy = (SqlOrderBy) node;
                SqlSelect query = (SqlSelect) orderBy.query;
                query.setOrderBy(orderBy.orderList);
                query.setOffset(orderBy.offset);
                query.setFetch(orderBy.fetch);
                return query;
            }
            return null;
        } catch (SqlParseException e) {
            return null;
        }
    }
}
