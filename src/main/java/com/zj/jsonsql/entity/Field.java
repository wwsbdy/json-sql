package com.zj.jsonsql.entity;

import com.zj.jsonsql.enums.JsonEnum;
import lombok.Data;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 列数据
 *
 * @author arthur_zhou
 */
@Data
public class Field {
    /**
     * 原始字段信息
     */
    private SqlNode originalFiled;
    /**
     * 展示名称
     */
    private String name;

    /**
     * 类型
     */
    private JsonEnum type;

    public Field(SqlNode originalFiled, String name) {
        this.originalFiled = originalFiled;
        this.name = name;
    }

    public Field(SqlNode originalFiled, String name, JsonEnum type) {
        this.originalFiled = originalFiled;
        this.name = name;
        this.type = type;
    }

    /**
     * 获取原始列
     * 1.如果列的类型全部相等，直接返回
     * 2.如果有不同类型，除开为NULL的，全部相等，直接返回
     * 3.除开NULL还是有不一样的，返回未知
     *
     * @param columnMap key：json的key，value：json的key的所有类型
     * @return 处理好的列信息
     */
    public static List<Field> getOriginalField(Map<String, List<JsonEnum>> columnMap) {
        if (MapUtils.isEmpty(columnMap)) {
            return Collections.emptyList();
        }
        List<Field> fields = new ArrayList<>();
        columnMap.forEach((k, v) -> {
            if (CollectionUtils.isEmpty(v)) {
                return;
            }
            List<JsonEnum> typeList = v.stream()
                    .distinct()
                    .filter(var -> JsonEnum.NULL != var)
                    .collect(Collectors.toList());
            SqlIdentifier sqlIdentifier = new SqlIdentifier(k, SqlParserPos.ZERO);
            if (CollectionUtils.isEmpty(typeList)) {
                fields.add(new Field(sqlIdentifier, k, JsonEnum.NULL));
            } else if (typeList.size() == 1) {
                fields.add(new Field(sqlIdentifier, k, typeList.get(0)));
            } else {
                fields.add(new Field(sqlIdentifier, k, JsonEnum.UNKNOWN));
            }
        });
        return fields;
    }
}
