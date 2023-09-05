package com.zj.demoplugin.utils;

import com.zj.demoplugin.entity.Field;
import com.zj.demoplugin.entity.MyJson;
import com.zj.demoplugin.strategy.AbstractStrategy;
import com.zj.demoplugin.strategy.StrategyBean;
import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

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
                // 保持原有大小写
                .withCaseSensitive(false);
    }

    /**
     * 获取满足条件的数据
     *
     * @param dataList
     * @param sqlNode
     * @return
     */
    public static List<MyJson> getDataList(List<MyJson> dataList, SqlSelect sqlNode) {
        Objects.requireNonNull(sqlNode);
        if (CollectionUtils.isEmpty(dataList)) {
            return Collections.emptyList();
        }
        SqlNode where = sqlNode.getWhere();
        AbstractStrategy strategy = StrategyBean.getStrategy((SqlBasicCall) where);
        Stream<MyJson> stream = dataList.stream().filter(strategy::apply);
//        if (Objects.nonNull(sql.getLimit())) {
//            stream = stream.skip(sql.getLimit().getFrom()).limit(sql.getLimit().getSize());
//        }
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
        Map<String, String> typeMap = columns.stream().collect(Collectors.toMap(Field::getOriginalName, Field::getType, (v1, v2) -> v2));
        // 取交集
        select.removeIf(v -> {
            String type = typeMap.get(v.getOriginalName());
            if (StringUtils.isEmpty(type)) {
                return true;
            }
            v.setType(type);
            return false;
        });
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
            // 不支持!= 可以使用 <>
            if (SqlKind.SELECT == kind) {
                return (SqlSelect) node;
            }
            return null;
        } catch (SqlParseException e) {
            return null;
        }
    }
}
