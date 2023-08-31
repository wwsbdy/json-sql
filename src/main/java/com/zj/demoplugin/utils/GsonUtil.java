package com.zj.demoplugin.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * @author xiehai
 * @date 2022/06/29 11:00
 */
public class GsonUtil {

    private static class Singleton {
        private static final Gson GSON = new Gson();
        public static Gson getGSON() {
            return GSON;
        }
    }

    /**
     * gson序列化
     *
     * @param o 任意对象
     * @return json字符串
     */
    public static String serialize(Object o) {
        return Singleton.getGSON().toJson(o);
    }

    /**
     * gson反序列化
     *
     * @param json  json字符串
     * @param clazz 目标类型
     * @param <T>   目标类型
     * @return {@link T}
     */
    public static <T> T deserialize(String json, Class<T> clazz) {
        return Singleton.getGSON().fromJson(json, clazz);
    }

    public static <T> T deepCopy(T t, Class<T> clazz) {
        return GsonUtil.deserialize(GsonUtil.serialize(t), clazz);
    }

    public static JsonElement parse(String jsonStr) {
        return JsonParser.parseString(jsonStr);
    }
}
