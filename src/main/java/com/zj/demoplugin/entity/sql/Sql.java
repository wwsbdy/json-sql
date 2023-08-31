package com.zj.demoplugin.entity.sql;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author arthur_zhou
 */
@Data
@NoArgsConstructor
public class Sql {
    /**
     * sql
     */
    private String str;
    /**
     * 查询字段
     */
    private List<String> select;
    /**
     * where条件
     */
    private List<Where> where;
    /**
     * 排序
     */
    private List<Order> order;
    /**
     * 限制条数
     */
    private Limit limit;

    public Sql(String sql) {
        this.str = sql;
    }
}
