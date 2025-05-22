package com.zj.jsonsql.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.zj.jsonsql.constant.Constant;
import com.zj.jsonsql.entity.Field;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.FuncEnum;
import com.zj.jsonsql.enums.JsonEnum;
import com.zj.jsonsql.enums.NoticeEnum;
import com.zj.jsonsql.exception.SqlException;
import com.zj.jsonsql.strategy.AbstractWhereStrategy;
import com.zj.jsonsql.strategy.SortStrategy;
import com.zj.jsonsql.strategy.StrategyBean;
import com.zj.jsonsql.ui.PluginBundle;
import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.util.NlsString;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
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
        // where校验字段是否存在
        findNotExistOrErrorFiled(where, columns.stream().map(Field::getName).collect(Collectors.toSet()));
        AbstractWhereStrategy strategy = StrategyBean.getStrategy(where);
        Stream<Row> stream = dataList.stream().filter(strategy::apply);
        // 获取查询的字段
        List<Field> selectList = getSelectList(columns, sqlNode);
        // 去重
        if (Objects.nonNull(sqlNode.getModifierNode(SqlSelectKeyword.DISTINCT))) {
            stream = stream.collect(Collectors.toMap(myJson -> {
                JSONObject jsonObject = new JSONObject();
                for (Field select : selectList) {
                    jsonObject.put(select.getName(), myJson.get(select.getOriginalFiled()));
                }
                return jsonObject;
            }, v -> v, (v1, v2) -> v1, LinkedHashMap::new)).values().stream();
        }
        // 追加表字段
        List<Field> selectAndColumnList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(columns)) {
            selectAndColumnList.addAll(columns);
        }
        selectAndColumnList.addAll(selectList);
        // 获取别名和原始名
        Map<String, SqlNode> nameMap = selectAndColumnList.stream()
                .collect(Collectors.toMap(Field::getName, Field::getOriginalFiled, (v1, v2) -> v2));
        // group
        SqlNodeList groupList = sqlNode.getGroup();
        if (CollectionUtils.isNotEmpty(groupList)) {
            Map<JSONObject, List<Row>> groupMap = stream.collect(Collectors.groupingBy(row -> {
                JSONObject jsonObject = new JSONObject();
                for (SqlNode group : groupList) {
                    SqlNode originalGroup = replaceAlias(group, nameMap);
                    jsonObject.put(originalGroup.toString(), row.get(originalGroup));
                }
                return jsonObject;
            }));
            stream = groupMap.values().stream()
                    .map(rows -> {
                        JSONObject jsonObject = Optional.of(rows)
                                .filter(CollectionUtils::isNotEmpty)
                                .map(v -> v.get(0))
                                .map(Row::getJsonObject)
                                .orElse(new JSONObject());
                        return new Row(jsonObject, rows);
                    });
        } else {
            // 只有两种情况，要不全部是聚合函数，要不全部不是聚合函数
            if (selectList.stream().map(Field::getOriginalFiled).allMatch(SqlUtil::existAggregateFunc)) {
                stream = stream.collect(Collectors.collectingAndThen(Collectors.toList(),
                        rows -> Collections.singletonList(new Row(rows.get(0).getJsonObject(), rows))
                )).stream();
            } else if (selectList.stream().map(Field::getOriginalFiled).anyMatch(SqlUtil::existAggregateFunc)) {
                throw new SqlException(PluginBundle.get("error.message.cant-have-agg-func"));
            }
        }
        // having
        if (Objects.nonNull(sqlNode.getHaving())) {
            SqlNode having = replaceAlias(sqlNode.getHaving(), nameMap);
            AbstractWhereStrategy havingStrategy = StrategyBean.getStrategy(having);
            stream = stream.filter(havingStrategy::apply);
        }
        // 排序
        SqlNodeList orderList = sqlNode.getOrderList();
        if (CollectionUtils.isNotEmpty(orderList)) {
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
        Map<String, JsonEnum> typeMap = columns.stream()
                .collect(Collectors.toMap(v -> v.getOriginalFiled().toString(), Field::getType, (v1, v2) -> v2));
        // 取交集
        for (Field field : select) {
            SqlNode originalFiled = field.getOriginalFiled();
            if (originalFiled instanceof SqlIdentifier) {
                String originalName = originalFiled.toString();
                if (typeMap.containsKey(originalName)) {
                    field.setType(typeMap.get(originalName));
                    continue;
                }
                String[] keys = originalName.split("\\.");
                if (keys.length == 0) {
                    throw new SqlException(originalName + PluginBundle.get("error.message.filed-error"));
                }
                JsonEnum type = typeMap.get(keys[0]);
                if (Objects.isNull(type)) {
                    throw new SqlException(originalName + PluginBundle.get("error.message.filed-no-find"));
                }
                field.setType(JsonEnum.INNER);
                continue;
            }
            if (originalFiled instanceof SqlLiteral) {
                SqlLiteral sqlLiteral = (SqlLiteral) originalFiled;
                Object value = sqlLiteral.getValue();
                if (value instanceof NlsString) {
                    value = ((NlsString) value).getValue().replaceAll("^'|'$", "");
                }
                field.setType(JsonUtil.getType(value));
                continue;
            }
            if (originalFiled instanceof SqlBasicCall) {
                SqlBasicCall sqlBasicCall = (SqlBasicCall) originalFiled;
                JsonEnum jsonEnum = Optional.ofNullable(sqlBasicCall.getOperator())
                        .map(SqlOperator::getName)
                        .map(FuncEnum::getByName)
                        .map(FuncEnum::getJsonEnum)
                        .orElse(JsonEnum.FUNC);
                field.setType(jsonEnum);
                continue;
            }
            field.setType(JsonEnum.UNKNOWN);
        }
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
        Set<String> columnSet = columns.stream().map(Field::getName).collect(Collectors.toSet());
        for (SqlNode node : sqlNode.getSelectList()) {
            String name = node.toString();
            // 查全部
            if (SELECT_ALL.equals(name)) {
                select.addAll(columns);
                continue;
            }
            if (node.getKind() == SqlKind.AS) {
                List<SqlNode> operandList = ((SqlBasicCall) node).getOperandList();
                if (CollectionUtils.isNotEmpty(operandList) && operandList.size() == 2) {
                    findNotExistOrErrorFiled(operandList.get(0), columnSet);
                    select.add(new Field(operandList.get(0), String.valueOf(operandList.get(1))));
                }
            } else {
                findNotExistOrErrorFiled(node, columnSet);
                select.add(new Field(node, name));
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
     * @param e 异常
     * @return 异常信息的第一行
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

    /**
     * 获取值
     *
     * @param sqlNode sql解析树
     * @return 查询值
     */
    public static Object toString(SqlNode sqlNode) {
        if (Objects.isNull(sqlNode)) {
            return null;
        }
        if (sqlNode instanceof SqlIdentifier) {
            return ((SqlIdentifier) sqlNode).getSimple();
        }
        SqlLiteral sqlLiteral = (SqlLiteral) sqlNode;
        Object value = sqlLiteral.getValue();
        if (value instanceof NlsString) {
            value = ((NlsString) value).getValue().replaceAll("^'|'$", "");
        }
        if (value instanceof Number) {
            return new BigDecimal(String.valueOf(value));
        }
        return Objects.isNull(value) ? null : String.valueOf(value);
    }

    /**
     * 是否存在聚合函数
     *
     * @param sqlNode sql解析树
     * @return 是否存在聚合函数
     */
    private static boolean existAggregateFunc(SqlNode sqlNode) {
        if (sqlNode instanceof SqlBasicCall) {
            SqlBasicCall sqlBasicCall = (SqlBasicCall) sqlNode;
            if (FuncEnum.isAggregateFunc(sqlBasicCall.getOperator().getName())) {
                return true;
            }
            for (SqlNode node : sqlBasicCall.getOperandList()) {
                if (existAggregateFunc(node)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否存在不存在或错误函数的字段
     *
     * @param sqlNode   sql解析树
     * @param columnSet 原始表字段
     */
    private static void findNotExistOrErrorFiled(SqlNode sqlNode, Set<String> columnSet) {
        if (Objects.isNull(sqlNode) || CollectionUtils.isEmpty(columnSet)) {
            return;
        }
        // 带.的函数字段不支持
        if (sqlNode.getKind() == SqlKind.DOT) {
            throw new SqlException(sqlNode + PluginBundle.get("error.message.field-no-support"));
        }
        if (sqlNode instanceof SqlIdentifier && !"*".equals(sqlNode.toString())) {
            SqlIdentifier sqlIdentifier = (SqlIdentifier) sqlNode;
            if (CollectionUtils.isNotEmpty(sqlIdentifier.names) && !columnSet.contains(sqlIdentifier.names.get(0))) {
                throw new SqlException(sqlNode + PluginBundle.get("error.message.filed-no-find"));
            }
            return;
        }
        if (sqlNode instanceof SqlBasicCall) {
            SqlBasicCall sqlBasicCall = (SqlBasicCall) sqlNode;
            // 如果是函数，判断是否支持
            Optional.ofNullable(StrategyBean.getFuncStrategy(sqlBasicCall.getOperator()))
                    .ifPresent(funcStrategy -> {
                        if (!funcStrategy.isSupport(sqlBasicCall.getOperandList())) {
                            throw new SqlException(funcStrategy.getType().name() + PluginBundle.get("error.message.func-param-error"));
                        }
                    });
            for (SqlNode node : sqlBasicCall.getOperandList()) {
                findNotExistOrErrorFiled(node, columnSet);
            }
        }
    }

    /**
     * 替换别名
     *
     * @param key     SqlNode
     * @param nameMap nameMap
     * @return SqlNode
     */
    public static SqlNode replaceAlias(SqlNode key, Map<String, SqlNode> nameMap) {
        if (Objects.isNull(key) || MapUtils.isEmpty(nameMap)) {
            return key;
        }
        if (key instanceof SqlIdentifier && !"*".equals(key.toString())) {
            if (nameMap.containsKey(key.toString())) {
                return nameMap.get(key.toString());
            }
            ImmutableList<String> names = ((SqlIdentifier) key).names;
            if (CollectionUtils.isNotEmpty(names) && nameMap.containsKey(names.get(0))) {
                SqlNode sqlNode = nameMap.get(names.get(0));
                if (sqlNode instanceof SqlIdentifier) {
                    List<String> newNames = new ArrayList<>(names);
                    newNames.set(0, sqlNode.toString());
                    return new SqlIdentifier(newNames, key.getParserPosition());
                } else {
                    throw new SqlException(key + PluginBundle.get("error.message.field-no-support"));
                }
            }
            throw new SqlException(key + PluginBundle.get("error.message.filed-no-find"));
        }
        // 带.的函数字段不支持
        if (key.getKind() == SqlKind.DOT) {
            throw new SqlException(key + PluginBundle.get("error.message.field-no-support"));
        }
        if (key instanceof SqlBasicCall) {
            SqlBasicCall sqlBasicCall = (SqlBasicCall) key;
            // 如果是函数，判断是否支持
            Optional.ofNullable(StrategyBean.getFuncStrategy(sqlBasicCall.getOperator()))
                    .ifPresent(funcStrategy -> {
                        if (!funcStrategy.isSupport(sqlBasicCall.getOperandList())) {
                            throw new SqlException(funcStrategy.getType().name() + PluginBundle.get("error.message.func-param-error"));
                        }
                    });
            List<SqlNode> operandList = sqlBasicCall.getOperandList();
            for (int i = 0; i < operandList.size(); i++) {
                SqlNode sqlNode = operandList.get(i);
                sqlBasicCall.setOperand(i, replaceAlias(sqlNode, nameMap));
            }
        }
        return key;
    }
}
