package com.zj.jsonsql.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.io.OutputStream;
import java.util.*;

/**
 * easy excel工具类
 *
 * @author arthur_zhou
 * @date 2023/12/20 18:54
 */
public class EasyExcelUtil {

    @Data
    private static class HeaderTree {
        private String name;
        private Map<String, HeaderTree> children = new LinkedHashMap<>();

        public HeaderTree(String name) {
            this.name = name;
        }
    }

    /**
     * 写入数据
     *
     * @param jsonArrayStr jsonArray字符串
     * @param out          输出流
     */
    public static void write(String jsonArrayStr, OutputStream out) {
        write(jsonArrayStr, out, ExcelTypeEnum.XLSX);
    }

    /**
     * 写入数据
     *
     * @param jsonArrayStr jsonArray字符串
     * @param out          输出流
     */
    public static void write(String jsonArrayStr, OutputStream out, ExcelTypeEnum excelType) {
        JSONArray jsonArray = parseArrayOrderly(jsonArrayStr);
        List<List<String>> headerList = getHeaderList(jsonArray);
        List<List<String>> dataList = getDataList(headerList, jsonArray);

        EasyExcel.write(out)
                .excelType(excelType)
                // 这里放入动态头
                .head(headerList).sheet("Sheet1")
                .doWrite(dataList);
    }

    /**
     * 整理数据行
     *
     * @param headerList 表头
     * @param jsonArray  原始数据
     * @return 数据行
     */
    private static List<List<String>> getDataList(List<List<String>> headerList, JSONArray jsonArray) {
        if (CollectionUtils.isEmpty(headerList) || CollectionUtils.isEmpty(jsonArray)) {
            return Collections.emptyList();
        }
        List<List<String>> dataList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            List<String> data = new ArrayList<>();
            for (List<String> header : headerList) {
                data.add(getData(jsonObject, header));
            }
            dataList.add(data);
        }
        return dataList;
    }

    /**
     * 根据层级获取数据
     *
     * @param jsonObject 这一行
     * @param header     表头层
     * @return 数据
     */
    private static String getData(JSONObject jsonObject, List<String> header) {
        String value = null;
        JSONObject json = jsonObject;
        for (String key : header) {
            Object o = json.get(key);
            if (Objects.isNull(o)) {
                value = null;
            } else {
                value = String.valueOf(o);
            }
            if (o instanceof JSONObject) {
                json = (JSONObject) o;
            } else {
                break;
            }
        }
        return value;
    }

    /**
     * 获取表头列表
     *
     * @param jsonArray 原数据
     * @return 表头列表
     */
    private static List<List<String>> getHeaderList(JSONArray jsonArray) {
        if (CollectionUtils.isEmpty(jsonArray)) {
            return Collections.emptyList();
        }
        // 先组装成树结构防止重复表头
        HeaderTree headerTree = new HeaderTree(null);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            foreachJsonObject(headerTree, jsonObject);
        }
        // 再平铺
        return getHeader(Collections.emptyList(), headerTree);
    }

    /**
     * 平铺树结构
     *
     * @param parentList 上一级列表
     * @param headerTree 树结构
     * @return easy excel需要的表头格式
     */
    private static List<List<String>> getHeader(List<String> parentList, HeaderTree headerTree) {
        List<List<String>> headerList = new ArrayList<>();
        List<String> currentList = new ArrayList<>(parentList);
        if (Objects.nonNull(headerTree.getName())) {
            currentList.add(headerTree.getName());
        }
        Collection<HeaderTree> children = headerTree.getChildren().values();
        if (CollectionUtils.isEmpty(children)) {
            headerList.add(currentList);
        } else {
            for (HeaderTree child : children) {
                headerList.addAll(getHeader(currentList, child));
            }
        }
        return headerList;
    }

    /**
     * 递归获取json层级结构
     *
     * @param headerTree 层级结构
     * @param jsonObject json数据
     */
    private static void foreachJsonObject(HeaderTree headerTree, JSONObject jsonObject) {
        Map<String, HeaderTree> childrenMap = headerTree.getChildren();
        jsonObject.forEach((k, v) -> {
            HeaderTree child = childrenMap.get(k);
            if (Objects.isNull(child)) {
                child = new HeaderTree(k);
                childrenMap.put(k, child);
            }
            if (v instanceof JSONObject) {
                foreachJsonObject(child, (JSONObject) v);
            }
        });
    }


    /**
     * fastjson JSONArray有序排序
     */
    private static JSONArray parseArrayOrderly(String jsonStr) {
        //JSON 默认排序
        int defaultParserFeature = JSON.DEFAULT_PARSER_FEATURE;
        //设置Feature.OrderedField 按照字符串中的顺序排序
        JSON.DEFAULT_PARSER_FEATURE = Feature.config(JSON.DEFAULT_PARSER_FEATURE, Feature.OrderedField, true);
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        //JSON.DEFAULT_PARSER_FEATURE设置为全局设置  不确定影响范围，使用完还原默认值
        JSON.DEFAULT_PARSER_FEATURE = defaultParserFeature;
        return jsonArray;
    }
}
