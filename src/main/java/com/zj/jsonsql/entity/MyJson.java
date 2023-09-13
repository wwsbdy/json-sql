package com.zj.jsonsql.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zj.jsonsql.enums.JsonEnum;
import com.zj.jsonsql.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

/**
 * @author arthur_zhou
 */
public class MyJson {

    private int id;

    private boolean selected;

    private final JSONObject jsonObject;

    public MyJson(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public int getId() {
        return id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * 获取value
     *
     * @param key 多层用 . 隔开
     * @return value
     */
    public Object get(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        String[] keys = key.split("\\.");
        return get(keys);
    }

    @Nullable
    public Object get(String[] keys) {
        if (Objects.isNull(jsonObject) || Objects.isNull(keys) || keys.length == 0) {
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
