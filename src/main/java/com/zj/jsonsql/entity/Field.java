package com.zj.jsonsql.entity;

import lombok.Data;

/**
 * 列数据
 *
 * @author arthur_zhou
 */
@Data
public class Field {
    /**
     * 原始名称
     */
    private String originalName;
    /**
     * 展示名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    public Field(String originalName, String name) {
        this.originalName = originalName;
        this.name = name;
    }

    public Field(String originalName, String name, String type) {
        this.originalName = originalName;
        this.name = name;
        this.type = type;
    }
}
