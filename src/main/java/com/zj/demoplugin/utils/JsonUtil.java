package com.zj.demoplugin.utils;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
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

    /**
     * 获取字段类型
     *
     * @param jsonObject
     * @param key
     * @return
     */
    public static Object getType(JSONObject jsonObject, String key) {
        if (Objects.isNull(jsonObject) || Objects.isNull(key)) {
            return null;
        }
        Object value = jsonObject.get(key);
        if (Objects.isNull(value)) {
            return "NULL";
        }
        if (value instanceof Number) {
            return "number";
        }
        if (value instanceof CharSequence) {
            return "string";
        }
        return "object";
    }
}
