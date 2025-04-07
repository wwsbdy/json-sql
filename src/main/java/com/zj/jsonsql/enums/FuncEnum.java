package com.zj.jsonsql.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author arthur_zhou
 */

public enum FuncEnum {
    LEFT,
    RIGHT,
    LENGTH,
    CONCAT,
    IF,
    IFNULL,
    NULLIF,
    SUBSTR,
    SUBSTRING,
    UPPER,
    LOWER,
    ISNULL,

    SUM,
    AVG,
    MAX,
    MIN,
    COUNT,
//    GROUP_CONCAT,
//    ANY_VALUE,
//    GROUP_ARRAY,
    ;

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
        switch (name) {
            case "SUM":
            case "AVG":
            case "MAX":
            case "MIN":
            case "COUNT":
                return true;
            default:
                return false;
        }
    }
}
