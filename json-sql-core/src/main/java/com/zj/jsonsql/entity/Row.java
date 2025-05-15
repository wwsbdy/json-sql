package com.zj.jsonsql.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zj.jsonsql.enums.JsonEnum;
import com.zj.jsonsql.exception.SqlException;
import com.zj.jsonsql.strategy.StrategyBean;
import com.zj.jsonsql.ui.PluginBundle;
import com.zj.jsonsql.utils.JsonUtil;
import com.zj.jsonsql.utils.SqlUtil;
import lombok.Data;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 行数据
 *
 * @author arthur_zhou
 */
@Data
public class Row {
    /**
     * 序号
     */
    private int id;
    /**
     * 是否选中
     */
    private boolean selected;
    /**
     * 数据
     */
    private final JSONObject jsonObject;

    /**
     * 所有行；有group by 的情况，此为满足条件的行
     */
    private final List<Row> rows;

    public Row(JSONObject jsonObject, List<Row> rows) {
        this.jsonObject = JsonUtil.replaceAllKey(jsonObject, "\\.", "_NaN_");
        this.rows = rows;
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
        if (jsonObject.containsKey(key)) {
            return jsonObject.get(key);
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
        Object result;
        switch (jsonEnum) {
            case OBJECT:
                result = ((JSONObject) object).get(key);
                break;
            case ARRAY:
                JSONArray array = (JSONArray) object;
                JSONArray arr = new JSONArray();
                for (Object item : array) {
                    Object childObject = getObject(key, item);
                    // 如果是数组套数组，直接平铺好了
                    if (childObject instanceof JSONArray) {
                        arr.addAll((JSONArray) childObject);
                    } else {
                        arr.add(childObject);
                    }
                }
                result = CollectionUtils.isEmpty(arr) || arr.stream().noneMatch(Objects::nonNull) ? null : arr;
                break;
            default:
                throw new SqlException(object + PluginBundle.get("error.message.no-json"));
        }
        return result;
    }


    public Set<String> keySet() {
        return jsonObject.keySet();
    }

    /**
     * 获取对应列值
     *
     * @param key     SqlNode 列信息
     * @return value
     */
    public Object get(SqlNode key) {
        if (Objects.isNull(key)) {
            return null;
        }
        SqlKind kind = key.getKind();
        switch (kind) {
            case IDENTIFIER:
                return get(key.toString());
            case DOT:
                throw new SqlException(key + PluginBundle.get("error.message.field-no-support"));
            case PLUS:
            case MINUS:
            case TIMES:
            case DIVIDE:
            case MOD:
            case GROUP_CONCAT:
            case OTHER_FUNCTION:
                return StrategyBean.getFuncStrategy(((SqlBasicCall) key).getOperator())
                        .get(this, ((SqlBasicCall) key));
        }
        // where条件
        if (key instanceof SqlBasicCall) {
            return StrategyBean.getStrategy(key).apply(this);
        }
        // 常量
        if (key instanceof SqlLiteral) {
            return SqlUtil.toString(key);
        }
        return get(key.toString());
    }
}
