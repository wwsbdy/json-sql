package com.zj.jsonsql.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.zj.jsonsql.constant.Constant;
import com.zj.jsonsql.entity.ExportInfo;
import com.zj.jsonsql.entity.Field;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.enums.JsonEnum;
import com.zj.jsonsql.enums.NoticeEnum;
import com.zj.jsonsql.exception.JsonException;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 19242
 */
public class JsonUtil {

    public static final JSONObject EMPTY_JSON_OBJECT = new JSONObject();
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
     * @param object 数据
     * @return 类型枚举
     */
    public static @NotNull JsonEnum getType(Object object) {
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
     * @param object 数据
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
     * @param jsonInfo   Json信息
     * @param exportInfo 导出设置
     * @return 转换的JsonStr
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
        List<Row> rows = SqlUtil.getRow(jsonInfo, realExportInfo.getRow());
        if (CollectionUtils.isEmpty(rows)) {
            return EMPTY_JSON_ARRAY_STR;
        }
        // 获取列
        List<Field> columns = SqlUtil.getColumn(jsonInfo, realExportInfo.getColumn());
        // 组装数据
        JSONArray jsonArray = getJsonArray(rows, columns, realExportInfo.isRound());
        // 是否去重
        if (realExportInfo.isDistinct()) {
            jsonArray = jsonArray.stream().distinct().collect(Collectors.toCollection(JSONArray::new));
        }
        JSON result = jsonArray;
        // 当只有一个元素时，只要不要[]
        if (realExportInfo.isOnlyOne() && CollectionUtils.isNotEmpty(jsonArray) && jsonArray.size() == 1) {
            Object o = jsonArray.get(0);
            if (o instanceof JSON) {
                result = (JSON) o;
            }
        }
        List<SerializerFeature> features = new ArrayList<>();
        features.add(SerializerFeature.WriteMapNullValue);
        // 使用单引号
        if (realExportInfo.isSingleQuotes()) {
            features.add(SerializerFeature.UseSingleQuotes);
        }
        // 是否美化
        if (realExportInfo.isBeautify()) {
            features.add(SerializerFeature.PrettyFormat);
        }
        return result.toString(features.toArray(new SerializerFeature[0]));
    }

    /**
     * 根据行和列组装JsonArray
     *
     * @param rows    要组装的行
     * @param columns 要组装的列
     * @param round   true且columns只有一个时，平铺
     * @return 组装好的JsonArray
     */
    private static JSONArray getJsonArray(List<Row> rows, List<Field> columns, boolean round) {
        JSONArray jsonArray = new JSONArray();
        if (round && CollectionUtils.isNotEmpty(columns) && columns.size() == 1) {
            SqlNode singleColumn = columns.get(0).getOriginalFiled();
            for (Row row : rows) {
                Object value = null;
                try {
                    value = row.get(singleColumn);
                } catch (Exception ignored) {
                }
                jsonArray.addAll(round(value));
            }
            return jsonArray;
        }
        for (Row row : rows) {
            if (CollectionUtils.isEmpty(columns)) {
                jsonArray.add(EMPTY_JSON_OBJECT);
                continue;
            }
            JSONObject jsonObject = new JSONObject(true);
            for (Field column : columns) {
                Object value = null;
                try {
                    value = row.get(column.getOriginalFiled());
                } catch (Exception ignored) {
                }
                jsonObject.put(column.getName().replaceAll("_NaN_", "."), value);
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 平铺
     *
     * @param o 数据
     * @return 平铺的数据
     */
    private static List<?> round(Object o) {
        JsonEnum type = getType(o);
        switch (type) {
            case OBJECT:
                JSONObject jsonObject = (JSONObject) o;
                if (jsonObject.size() == 1) {
                    return round(jsonObject.values().stream().findFirst().orElse(null));
                }
                return Collections.singletonList(o);
            case ARRAY:
                List<Object> list = new ArrayList<>();
                JSONArray jsonArray = (JSONArray) o;
                for (Object o1 : jsonArray) {
                    list.addAll(round(o1));
                }
                return list;
            default:
                return Collections.singletonList(o);
        }
    }

    /**
     * 替换key值
     *
     * @param jsonObject  json数据
     * @param regex       正则
     * @param replacement 替换为
     * @return 替换后的json数据
     */
    public static JSONObject replaceAllKey(JSONObject jsonObject, String regex, String replacement) {
        if (Objects.isNull(jsonObject)) {
            return null;
        }
        JSONObject resultJsonObject = new JSONObject(true);
        jsonObject.forEach((k, v) -> resultJsonObject.put(k.replaceAll(regex, replacement), v));
        return resultJsonObject;
    }

    public static JsonInfo getJsonInfo(String jsonStr) throws JsonException {
        if (StringUtils.isEmpty(jsonStr)) {
            throw new JsonException(NoticeEnum.JSON_EMPTY);
        }
        JSONArray jsonArray = null;
        try {
            Object parse = JSON.parse(jsonStr, Feature.OrderedField);
            if (parse instanceof JSONObject) {
                jsonArray = new JSONArray();
                jsonArray.add(parse);
            } else if (parse instanceof JSONArray) {
                jsonArray = JSONArray.parseArray(jsonStr, Feature.OrderedField);
            }
        } catch (Exception exception) {
            throw new JsonException(NoticeEnum.JSON_ERROR);
        }
        if (CollectionUtils.isEmpty(jsonArray)) {
            throw new JsonException(NoticeEnum.JSON_EMPTY);
        }
        if (jsonArray.size() > Constant.ROWS_MAX) {
            throw new JsonException(NoticeEnum.ROWS_TOO_MANY);
        }
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
        if (MapUtils.isEmpty(columnMap) || CollectionUtils.isEmpty(rowList)) {
            throw new JsonException(NoticeEnum.JSON_EMPTY);
        }
        if (columnMap.size() > Constant.COLUMNS_MAX) {
            throw new JsonException(NoticeEnum.COLUMNS_TOO_MANY);
        }
        return new JsonInfo(Field.getOriginalField(columnMap), rowList, jsonStr);
    }
}
