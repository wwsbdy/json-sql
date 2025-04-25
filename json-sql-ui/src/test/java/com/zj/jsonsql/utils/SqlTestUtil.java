package com.zj.jsonsql.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.collect.Lists;
import com.intellij.util.ui.ColumnInfo;
import com.zj.jsonsql.constant.Constant;
import com.zj.jsonsql.entity.Field;
import com.zj.jsonsql.entity.IdeaJsonInfo;
import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.JsonEnum;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParseException;

import java.util.*;

public class SqlTestUtil {

    public static IdeaJsonInfo getJsonInfo(String sql) {
        return getJsonInfo("[{\"id\":2484,\"code\":\"510100\",\"name\":\"成都市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2506,\"code\":\"510300\",\"name\":\"自贡市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2514,\"code\":\"510400\",\"name\":\"攀枝花市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2521,\"code\":\"510500\",\"name\":\"泸州市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2530,\"code\":\"510600\",\"name\":\"德阳市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2538,\"code\":\"510700\",\"name\":\"绵阳市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2549,\"code\":\"510800\",\"name\":\"广元市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2558,\"code\":\"510900\",\"name\":\"遂宁市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2565,\"code\":\"511000\",\"name\":\"内江市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2572,\"code\":\"511100\",\"name\":\"乐山市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2585,\"code\":\"511300\",\"name\":\"南充市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2596,\"code\":\"511400\",\"name\":\"眉山市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2604,\"code\":\"511500\",\"name\":\"宜宾市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2616,\"code\":\"511600\",\"name\":\"广安市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2624,\"code\":\"511700\",\"name\":\"达州市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2633,\"code\":\"511800\",\"name\":\"雅安市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2643,\"code\":\"511900\",\"name\":\"巴中市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2650,\"code\":\"512000\",\"name\":\"资阳市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2655,\"code\":\"513200\",\"name\":\"阿坝藏族羌族自治州\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":null,\"isChoosed\":0,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2669,\"code\":\"513300\",\"name\":\"甘孜藏族自治州\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":null,\"isChoosed\":0,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2688,\"code\":\"513400\",\"name\":\"凉山彝族自治州\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null}]",
                sql);
    }

    public static IdeaJsonInfo getJsonInfo(String jsonStr, String sql) {
        JSONArray jsonArray = JSONArray.parseArray(jsonStr, Feature.OrderedField);

        List<Row> rowList = new ArrayList<>();
        Map<String, List<JsonEnum>> columnMap = new LinkedHashMap<>();
        String onlyFiled = null;
        for (Object o : jsonArray) {
            Row row;
            if (Objects.isNull(o) || !(o instanceof JSONObject)) {
                // 不是JSONObject, 新定义一个JSONObject放入
                if (Objects.isNull(onlyFiled)) {
                    onlyFiled = Constant.ONLY_FILED + System.currentTimeMillis() / 1000L;
                }
                row = new Row(new JSONObject().fluentPut(onlyFiled, o), rowList);
            } else {
                row = new Row((JSONObject) o, rowList);
            }
            for (String key : row.keySet()) {
                // 可能会出现不同数据里同一个key，value不一样的情况。如：null和string。这时以不是null的为准，其他的情况以最后一个value类型为准
                JsonEnum type = JsonUtil.getType(row.get(key));
                if (columnMap.containsKey(key)) {
                    columnMap.get(key).add(type);
                } else {
                    columnMap.put(key, Lists.newArrayList(type));
                }
            }
            rowList.add(row);
        }
        IdeaJsonInfo jsonInfo = new IdeaJsonInfo(Field.getOriginalField(columnMap), rowList, jsonStr);
        SqlSelect sqlSelect;
        try {
            sqlSelect = SqlUtil.toSqlSelect(sql);
        } catch (SqlParseException e) {
            throw new RuntimeException(e);
        }
        jsonInfo.setSql(sql);
        jsonInfo.setSqlNode(sqlSelect);
        return jsonInfo;
    }

    public static JSONArray getJsonArray(IdeaJsonInfo jsonInfo) {
        ColumnInfo<Row, ?>[] fields = jsonInfo.getFields();
        List<Row> rows = jsonInfo.getRows();
        JSONArray objects = new JSONArray();
        for (Row row : rows) {
            JSONObject jsonObject = new JSONObject(true);
            for (ColumnInfo<Row, ?> field : fields) {
                jsonObject.put(field.getName(), field.valueOf(row));
            }
            objects.add(jsonObject);
        }
        return objects;
    }

    public static void out(String sql) {
        IdeaJsonInfo jsonInfo = getJsonInfo(sql);
        ColumnInfo<Row, ?>[] fields = jsonInfo.getFields();
        for (ColumnInfo<Row, ?> field : fields) {
            System.out.print(field.getName());
            System.out.print("\t\t");
        }
        System.out.println();
        List<Row> rows = jsonInfo.getRows();
        for (Row row : rows) {
            for (ColumnInfo<Row, ?> field : fields) {
                System.out.print(field.valueOf(row));
                System.out.print("\t\t");
            }
            System.out.println();
        }
    }
}
