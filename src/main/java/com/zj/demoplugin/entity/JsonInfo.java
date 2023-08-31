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

    private List<String> columns;
    /**
     * 导入数据列表
     */
    private List<JSONObject> list;


    public JsonInfo(List<String> columns, List<JSONObject> list) {
        this.sql = new Sql("select * from arr");
        this.columns = columns;
        this.list = list;
    }
}
