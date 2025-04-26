package com.zj.jsonsql.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.Feature;
import com.zj.jsonsql.entity.Field;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.entity.Row;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParseException;

import java.util.List;

public class SqlTestUtil {

    public static JsonInfo getJsonInfo(String sql) {
        JsonInfo jsonInfo = JsonUtil.getJsonInfo("[{\"id\":2484,\"code\":\"510100\",\"name\":\"成都市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2506,\"code\":\"510300\",\"name\":\"自贡市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2514,\"code\":\"510400\",\"name\":\"攀枝花市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2521,\"code\":\"510500\",\"name\":\"泸州市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2530,\"code\":\"510600\",\"name\":\"德阳市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2538,\"code\":\"510700\",\"name\":\"绵阳市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2549,\"code\":\"510800\",\"name\":\"广元市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2558,\"code\":\"510900\",\"name\":\"遂宁市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2565,\"code\":\"511000\",\"name\":\"内江市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2572,\"code\":\"511100\",\"name\":\"乐山市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2585,\"code\":\"511300\",\"name\":\"南充市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2596,\"code\":\"511400\",\"name\":\"眉山市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2604,\"code\":\"511500\",\"name\":\"宜宾市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2616,\"code\":\"511600\",\"name\":\"广安市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2624,\"code\":\"511700\",\"name\":\"达州市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2633,\"code\":\"511800\",\"name\":\"雅安市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2643,\"code\":\"511900\",\"name\":\"巴中市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2650,\"code\":\"512000\",\"name\":\"资阳市\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2655,\"code\":\"513200\",\"name\":\"阿坝藏族羌族自治州\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":null,\"isChoosed\":0,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2669,\"code\":\"513300\",\"name\":\"甘孜藏族自治州\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":null,\"isChoosed\":0,\"isDisabled\":null,\"childList\":null,\"children\":null},{\"id\":2688,\"code\":\"513400\",\"name\":\"凉山彝族自治州\",\"parentId\":2483,\"parentName\":\"四川省\",\"groupName\":\"华西\",\"isChoosed\":1,\"isDisabled\":null,\"childList\":null,\"children\":null}]");

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
