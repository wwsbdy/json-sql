package com.zj.demoplugin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author arthur_zhou
 */
@Data
@AllArgsConstructor
public class Field {
    /**
     * 原始名称
     */
    private String originalName;
    /**
     * 展示名称
     */
    private String name;
}
