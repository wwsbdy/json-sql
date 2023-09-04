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
     * @param value
     * @return
     */
    public static String getType(Object value) {
        if (Objects.isNull(value)) {
            return "NULL";
        }
        if (value instanceof Number) {
            return "number";
        }
        if (value instanceof CharSequence) {
            return "string";
        }
        if (value instanceof Boolean) {
            return "boolean";
        }
        return "object";
    }
}
