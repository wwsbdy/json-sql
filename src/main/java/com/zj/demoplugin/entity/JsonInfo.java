package com.zj.demoplugin.entity;

import com.alibaba.fastjson.JSONObject;
import com.zj.demoplugin.entity.sql.Sql;
import lombok.Data;

import java.util.List;

/**
 * json信息
 *
 * @author arthur_zhou
 */
@Data
public class JsonInfo {
    /**
     * sql语句
     */
    private Sql sql;
    /**
     * 导入数据列表
     */
    private List<JSONObject> list;

}
