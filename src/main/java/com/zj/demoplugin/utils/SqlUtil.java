package com.zj.demoplugin.utils;

import com.alibaba.fastjson.JSONObject;
import com.intellij.util.ui.ColumnInfo;
import com.zj.demoplugin.entity.Field;
import com.zj.demoplugin.entity.StrColumnInfo;
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
    public static List<JSONObject> getDataList(List<JSONObject> dataList, SqlSelect sqlNode) {
        Objects.requireNonNull(sqlNode);
        if (CollectionUtils.isEmpty(dataList)) {
            return Collections.emptyList();
        }
        return dataList;
        // todo
//        Stream<JSONObject> stream = dataList.stream().filter(sql::filter);
//        if (Objects.nonNull(sql.getLimit())) {
//            stream = stream.skip(sql.getLimit().getFrom()).limit(sql.getLimit().getSize());
//        }
//        return stream.collect(Collectors.toList());
    }

    /**
     * 获取字段名
     *
     * @param columns
     * @param sqlNode
     * @return
     */
    public static ColumnInfo<?, ?>[] getFields(List<String> columns, SqlSelect sqlNode) {
        Objects.requireNonNull(sqlNode);
        if (CollectionUtils.isEmpty(columns)) {
            return new ColumnInfo[0];
        }
        List<Field> select = new ArrayList<>();
        for (SqlNode node : sqlNode.getSelectList()) {
            String name = node.toString();
            // 查全部
            if ("*".equals(name)) {
                for (String column : columns) {
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
        HashSet<String> columnSet = new HashSet<>(columns);
        // 取交集
        select.removeIf(v -> !columnSet.contains(v.getOriginalName()));
        ColumnInfo<?, ?>[] columnInfos = new ColumnInfo[select.size()];
        for (int i = 0; i < select.size(); i++) {
            Field field = select.get(i);
            columnInfos[i] = new StrColumnInfo(field.getOriginalName(), field.getName());
        }
        return columnInfos;
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
