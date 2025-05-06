package com.zj.jsonsql.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.Feature;
import com.zj.jsonsql.entity.Field;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.entity.Row;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.commons.codec.Resources;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SqlTestUtil {

    private static final String jsonStr;

    static {
        try (InputStream fis = Resources.getInputStream("jiangsu.json")) {
            jsonStr = IOUtils.toString(fis, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonInfo getJsonInfo(String sql) {
        JsonInfo jsonInfo = JsonUtil.getJsonInfo(jsonStr);
        jsonInfo.setSql(sql);
        SqlSelect sqlSelect;
        try {
            sqlSelect = SqlUtil.toSqlSelect(sql);
        } catch (SqlParseException e) {
            throw new RuntimeException(e);
        }
        jsonInfo.setSqlNode(sqlSelect);
        return jsonInfo;

    }


    public static JSONArray getJsonArray(JsonInfo jsonInfo) {
        return JSONArray.parseArray(JsonUtil.getJsonStr(jsonInfo, null), Feature.OrderedField);
    }

    public static void out(String sql) {
        JsonInfo jsonInfo = getJsonInfo(sql);
        List<Field> select = jsonInfo.getSelect();
        for (Field field : select) {
            System.out.print(field.getName());
            System.out.print("\t\t");
        }
        System.out.println();
        List<Row> rows = jsonInfo.getResult();
        for (Row row : rows) {
            for (Field field : select) {
                System.out.print(row.get(field.getOriginalFiled()));
                System.out.print("\t\t");
            }
            System.out.println();
        }
    }
}
