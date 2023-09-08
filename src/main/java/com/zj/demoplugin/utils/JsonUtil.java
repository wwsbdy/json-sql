package com.zj.demoplugin.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zj.demoplugin.entity.ExportInfo;
import com.zj.demoplugin.entity.Field;
import com.zj.demoplugin.entity.JsonInfo;
import com.zj.demoplugin.entity.MyJson;
import com.zj.demoplugin.enums.JsonEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author 19242
 */
public class JsonUtil {

    private static final JSONObject EMPTY_JSON_OBJECT = new JSONObject();
    private static final String EMPTY_JSON_ARRAY_STR = "[]";


    public static Object get(JSONObject jsonObject, String key) {
        if (Objects.isNull(jsonObject) || Objects.isNull(key)) {
            return null;
        }
        Object value = jsonObject.get(key);
        if (Objects.isNull(value)) {
            return null;
        }
        if (value instanceof Number) {
            return new BigDecimal(String.valueOf(value));
        }
        return value;
    }

    /**
     * 判断对象的json类型
     *
     * @param object
     * @return
     */
    public static JsonEnum getType(Object object) {
        if (Objects.isNull(object)) {
            return JsonEnum.NULL;
        }
        if (object instanceof Number) {
            return JsonEnum.NUMBER;
        }
        if (object instanceof String) {
            return JsonEnum.STRING;
        }
        if (object instanceof JSONArray) {
            return JsonEnum.ARRAY;
        }
        if (object instanceof JSONObject) {
            return JsonEnum.OBJECT;
        }
        if (object instanceof Boolean) {
            return JsonEnum.BOOLEAN;
        }
        return JsonEnum.UNKNOWN;
    }

    /**
     * 转换类型
     *
     * @param object
     * @return 仅返回，Number、List、String
     */
    public static Object convert(Object object) {
        if (Objects.isNull(object)) {
            return null;
        }
        if (object instanceof Number) {
            return new BigDecimal(object.toString());
        }
        if (object instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) object;
            if (CollectionUtils.isEmpty(jsonArray)) {
                return null;
            }
            List<Object> list = new ArrayList<>();
            for (Object o : jsonArray) {
                Object convert = convert(o);
                if (Objects.nonNull(convert)) {
                    list.add(convert);
                }
            }
            return CollectionUtils.isEmpty(list) ? null : list;
        }
        return object.toString();
    }

    /**
     * 将jsonArray通过配置转成字符串
     *
     * @param jsonInfo
     * @param exportInfo
     * @return
     */
    public static String getJsonStr(JsonInfo jsonInfo, ExportInfo exportInfo) {
        if (Objects.isNull(jsonInfo) || CollectionUtils.isEmpty(jsonInfo.getList())) {
            return "";
        }
        ExportInfo realExportInfo;
        if (Objects.isNull(realExportInfo = exportInfo)) {
            realExportInfo = new ExportInfo();
        }
        // 获取行
        List<MyJson> rows = SqlUtil.getRow(jsonInfo, realExportInfo.getRow());
        if (CollectionUtils.isEmpty(rows)) {
            return EMPTY_JSON_ARRAY_STR;
        }
        // 获取列
        List<Field> columns = SqlUtil.getColumn(jsonInfo, realExportInfo.getColumn());
        // 组装数据
        JSONArray jsonArray = getJsonArray(rows, columns, realExportInfo.isRound());
        // 是否美化
        if (realExportInfo.isBeautify()) {
            return jsonArray.toString(SerializerFeature.PrettyFormat);
        }
        return jsonArray.toString();
    }

    /**
     * 根据行和列组装JsonArray
     *
     * @param rows
     * @param columns
     * @param round   true且columns只有一个时，平铺
     * @return
     */
    private static JSONArray getJsonArray(List<MyJson> rows, List<Field> columns, boolean round) {
        JSONArray jsonArray = new JSONArray();
        if (round && CollectionUtils.isNotEmpty(columns) && columns.size() == 1) {
            String singleColumn = columns.get(0).getOriginalName();
            for (MyJson row : rows) {
                if (StringUtils.isNotEmpty(singleColumn)) {
                    jsonArray.addAll(round(row.get(singleColumn)));
                }
            }
            return jsonArray;
        }
        for (MyJson row : rows) {
            if (CollectionUtils.isEmpty(columns)) {
                jsonArray.add(EMPTY_JSON_OBJECT);
                continue;
            }
            JSONObject jsonObject = new JSONObject();
            for (Field column : columns) {
                jsonObject.put(column.getName(), row.get(column.getOriginalName()));
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 平铺
     *
     * @param o
     * @return
     */
    private static List<?> round(Object o) {
        if (Objects.isNull(o)) {
            return Collections.emptyList();
        }
        JsonEnum type = getType(o);
        switch (type) {
            case OBJECT:
                JSONObject jsonObject = (JSONObject) o;
                if (jsonObject.size() == 1) {
                    return round(jsonObject.values().stream().findFirst().orElse(null));
                }
                return Collections.singletonList(o);
            case ARRAY:
                List list = new ArrayList<>();
                JSONArray jsonArray = (JSONArray) o;
                for (Object o1 : jsonArray) {
                    list.addAll(round(o1));
                }
                return list;
            default:
                return Collections.singletonList(o);
        }
    }
}
