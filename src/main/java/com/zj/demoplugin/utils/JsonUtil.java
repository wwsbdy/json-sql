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
}
