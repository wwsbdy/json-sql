package com.zj.jsonsql.utils;

import com.alibaba.fastjson.JSONObject;
import com.zj.jsonsql.constant.Constant;
import com.zj.jsonsql.entity.Field;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.JsonEnum;
import com.zj.jsonsql.enums.NoticeEnum;
import com.zj.jsonsql.strategy.AbstractWhereStrategy;
import com.zj.jsonsql.strategy.SortStrategy;
import com.zj.jsonsql.strategy.StrategyBean;
import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author arthur_zhou
 */
public class SqlUtil {

    private static final SqlParser.Config CONFIG;
    private static final String SELECT_ALL = "*";

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
     * @param dataList 原始数据
     * @param columns  原始字段
     * @param sqlNode  sql解析树
     * @return 过滤得到的数据
     */
    public static List<Row> getDataList(List<Row> dataList, List<Field> columns, SqlSelect sqlNode) {
        if (Objects.isNull(sqlNode)) {
            return dataList;
        }
        if (CollectionUtils.isEmpty(dataList)) {
            return Collections.emptyList();
        }
        // 过滤
        SqlNode where = sqlNode.getWhere();
        AbstractWhereStrategy strategy = StrategyBean.getStrategy((SqlBasicCall) where);
        Stream<Row> stream = dataList.stream().filter(strategy::apply);
        // 获取查询的字段
        List<Field> selectList = getSelectList(columns, sqlNode);
        // 去重
        if (Objects.nonNull(sqlNode.getModifierNode(SqlSelectKeyword.DISTINCT))) {
            stream = stream.collect(Collectors.toMap(myJson -> {
                JSONObject jsonObject = new JSONObject();
                for (Field select : selectList) {
                    jsonObject.put(select.getName(), myJson.get(select.getOriginalName()));
                }
                return jsonObject;
            }, v -> v, (v1, v2) -> v1, LinkedHashMap::new)).values().stream();
        }
        // 排序
        SqlNodeList orderList = sqlNode.getOrderList();
        if (CollectionUtils.isNotEmpty(orderList)) {
            // 追加表字段
            selectList.addAll(columns);
            // 获取别名和原始名
            Map<String, String> nameMap = selectList.stream()
                    .collect(Collectors.toMap(Field::getName, Field::getOriginalName, (v1, v2) -> v2));
            SortStrategy sortStrategy = new SortStrategy(orderList, nameMap);
            stream = stream.sorted(sortStrategy::orderBy);
        }
        // limit
        SqlNode offset = sqlNode.getOffset();
        if (Objects.nonNull(offset)) {
            stream = stream.skip(Long.parseLong(offset.toString()));
        }
        SqlNode fetch = sqlNode.getFetch();
        if (Objects.nonNull(fetch)) {
            stream = stream.limit(Long.parseLong(fetch.toString()));
        }
        AtomicInteger i = new AtomicInteger();
        return stream.peek(v -> {
            v.setId(i.incrementAndGet());
            v.setSelected(false);
        }).collect(Collectors.toList());
    }

    /**
     * 获取表头
     *
     * @param columns 原始字段
     * @param sqlNode sql解析树
     * @return 表头
     */
    public static List<Field> getFields(List<Field> columns, SqlSelect sqlNode) {
        if (Objects.isNull(sqlNode)) {
            return columns;
        }
        if (CollectionUtils.isEmpty(columns)) {
            return Collections.emptyList();
        }
        List<Field> select = getSelectList(columns, sqlNode);
        Map<String, JsonEnum> typeMap = columns.stream().collect(Collectors.toMap(Field::getOriginalName, Field::getType, (v1, v2) -> v2));
        // 取交集
        select.removeIf(v -> {
            if (StringUtils.isEmpty(v.getOriginalName())) {
                return true;
            }
            if (typeMap.containsKey(v.getOriginalName())) {
                v.setType(typeMap.get(v.getOriginalName()));
                return false;
            }
            String[] keys = v.getOriginalName().split("\\.");
            if (keys.length == 0) {
                return true;
            }
            JsonEnum type = typeMap.get(keys[0]);
            if (Objects.isNull(type)) {
                return true;
            }
            v.setType(JsonEnum.INNER);
            return false;
        });
        return select;
    }

    /**
     * 获取查询字段
     *
     * @param columns 原始字段
     * @param sqlNode sql解析树
     * @return 查询字段
     */
    @NotNull
    public static List<Field> getSelectList(List<Field> columns, SqlSelect sqlNode) {
        List<Field> select = new ArrayList<>();
        for (SqlNode node : sqlNode.getSelectList()) {
            String name = node.toString();
            // 查全部
            if (SELECT_ALL.equals(name)) {
                select.addAll(columns);
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
     * @param sql sql语句
     * @return sql解析树
     */
    public static SqlSelect toSqlSelect(String sql) throws SqlParseException {
        if (StringUtils.isEmpty(sql)) {
            return null;
        }
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
    }

    /**
     * 获取导出行
     *
     * @param jsonInfo json信息
     * @param row      0-SQL查询，1-勾选行，2-全部
     * @return 导出行
     */
    public static List<Row> getRow(JsonInfo jsonInfo, int row) {
        switch (row) {
            case 0:
                return jsonInfo.getResult();
            case 1:
                return jsonInfo.getResult().stream().filter(Row::isSelected).collect(Collectors.toList());
            case 2:
                return jsonInfo.getList();
            default:
                return Collections.emptyList();
        }
    }

    /**
     * 获取导出列
     *
     * @param jsonInfo json信息
     * @param column   0-SQL查询，1-全部
     * @return 导出列
     */
    public static List<Field> getColumn(JsonInfo jsonInfo, int column) {
        switch (column) {
            case 0:
                return jsonInfo.getSelect();
            case 1:
                return jsonInfo.getColumns();
            default:
                return Collections.emptyList();
        }
    }

    /**
     * 获取错误信息
     *
     * @param e
     * @return
     */
    public static String getErrorMessage(SqlParseException e) {
        String message = e.getMessage();
        if (StringUtils.isEmpty(message)) {
            return NoticeEnum.SQL_ERROR.getMessage();
        }
        String[] split = message.split("\r\n");
        if (split.length == 0) {
            return NoticeEnum.SQL_ERROR.getMessage();
        }
        String result = split[0];
        return result.length() > Constant.MESSAGE_MAX ? result.substring(0, Constant.MESSAGE_MAX) : result;
    }
}
