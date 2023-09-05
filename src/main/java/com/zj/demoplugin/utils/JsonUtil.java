package com.zj.demoplugin.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zj.demoplugin.enums.JsonEnum;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 19242
 */
public class JsonUtil {

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

    public static Object convert(Object object){
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
}
