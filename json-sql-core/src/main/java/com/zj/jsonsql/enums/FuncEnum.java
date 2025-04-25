package com.zj.jsonsql.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author arthur_zhou
 */

public enum FuncEnum {
    LEFT(0, JsonEnum.FUNC),
    RIGHT(0, JsonEnum.FUNC),
    LENGTH(0, JsonEnum.NUMBER),
    CONCAT(0, JsonEnum.STRING),
    IF(0, JsonEnum.FUNC),
    IFNULL(0, JsonEnum.FUNC),
    NULLIF(0, JsonEnum.FUNC),
    SUBSTR(0, JsonEnum.STRING),
    SUBSTRING(0, JsonEnum.STRING),
    UPPER(0, JsonEnum.STRING),
    LOWER(0, JsonEnum.STRING),
    ISNULL(0, JsonEnum.BOOLEAN),
    ROUND(0, JsonEnum.NUMBER),

    SUM(1, JsonEnum.NUMBER),
    AVG(1, JsonEnum.NUMBER),
    MAX(1, JsonEnum.FUNC),
    MIN(1, JsonEnum.FUNC),
    COUNT(1, JsonEnum.NUMBER),
    GROUP_CONCAT(1, JsonEnum.NUMBER),
    ANY_VALUE(1, JsonEnum.FUNC),
    GROUP_ARRAY(1, JsonEnum.ARRAY),
    ;

    /**
     * 0: 函数
     * 1: 聚合函数
     */
    private final int type;

    @Getter
    private final JsonEnum jsonEnum;

    FuncEnum(int type, JsonEnum jsonEnum) {
        this.type = type;
        this.jsonEnum = jsonEnum;
    }

    public static FuncEnum getByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        name = name.toUpperCase();
        for (FuncEnum funcEnum : values()) {
            if (funcEnum.name().equals(name)) {
                return funcEnum;
            }
        }
        return null;
    }

    public static boolean isAggregateFunc(String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        name = name.toUpperCase();
        for (FuncEnum funcEnum : values()) {
            if (funcEnum.type == 1 && funcEnum.name().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
