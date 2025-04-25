package com.zj.jsonsql.enums;

/**
 * @author arthur_zhou
 */

public enum JsonEnum {
    /**
     * jsonObject
     */
    OBJECT,
    /**
     * jsonArray
     */
    ARRAY,
    /**
     * String
     */
    STRING,
    /**
     * Number
     */
    NUMBER,
    /**
     * Boolean
     */
    BOOLEAN,
    /**
     * 未知
     */
    UNKNOWN,
    /**
     * null
     */
    NULL,
    /**
     * 嵌套，无法直接获取类型
     */
    INNER,
    /**
     * 函数
     */
    FUNC,
    ;
}
