package com.zj.demoplugin.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zj.demoplugin.enums.JsonEnum;
import com.zj.demoplugin.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

/**
 * @author arthur_zhou
 */
public class MyJson {

    private final JSONObject jsonObject;

    public MyJson(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    /**
     * 获取value
     *
     * @param key 多层用 . 隔开
     * @return
     */
    public Object get(String key) {
        if (Objects.isNull(jsonObject) || StringUtils.isEmpty(key)) {
            return null;
        }
        String[] keys = key.split("\\.");
        if (keys.length == 0) {
            return null;
        }
        Object object = jsonObject;
        for (String k : keys) {
            object = getObject(k, object);
            if (Objects.isNull(object)) {
                return null;
            }
        }
        return object;
    }

    @Nullable
    public Object getObject(String key, Object object) {
        JsonEnum jsonEnum = JsonUtil.getType(object);
        if (Objects.isNull(jsonEnum)) {
            return null;
        }
        Object result;
        switch (jsonEnum) {
            case OBJECT:
                result = ((JSONObject) object).get(key);
                break;
            case ARRAY:
                JSONArray array = (JSONArray) object;
                JSONArray arr = new JSONArray();
                for (Object item : array) {
                    Object value = getObject(key, item);
                    if (Objects.nonNull(value)) {
                        arr.add(value);
                    }
                }
                result = CollectionUtils.isEmpty(arr) ? null : arr;
                break;
            default:
                return null;
        }
        return result;
    }


    public Set<String> keySet() {
        return jsonObject.keySet();
    }
}
