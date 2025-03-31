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

    UPPER, LOWER;

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
}
