package com.zj.jsonsql.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : jie.zhou
 * @date : 2025/4/18
 */
@Getter
public class ImportReadListener extends AnalysisEventListener<Map<Integer, Object>> {

    private JSONArray jsonArray;
    private Map<Integer, String> headMap;
    private final List<Map<Integer, Object>> dataList = new ArrayList<>();

    @Override
    public void invoke(Map<Integer, Object> objectObjectMap, AnalysisContext analysisContext) {
        dataList.add(objectObjectMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (MapUtils.isEmpty(headMap) || CollectionUtils.isEmpty(dataList)) {
            return;
        }
        jsonArray = new JSONArray();
        for (Map<Integer, Object> data : dataList) {
            JSONObject jsonObject = new JSONObject(true);
            for (Integer key : headMap.keySet()) {
                jsonObject.put(headMap.get(key), data.get(key));
            }
            jsonArray.add(jsonObject);
        }
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headMap = headMap;
        super.invokeHeadMap(headMap, context);
    }
}
