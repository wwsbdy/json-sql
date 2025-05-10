package com.zj.jsonsql.entity;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.function.Consumer;

/**
 * @author 19242
 */
public class SqlHistoryList {

    private final int size;
    private final LinkedHashSet<String> set = new LinkedHashSet<>();

    public SqlHistoryList(int size) {
        this.size = size;
    }

    public boolean add(String s) {
        if (set.contains(s)) {
            set.remove(s);
            set.add(s);
            return true;
        }
        if (set.size() >= size) {
            set.remove(set.iterator().next());
        }
        return set.add(s);
    }

    public void forEach(Consumer<? super String> action) {
        // 倒序
        ArrayList<String> list = new ArrayList<>(set);
        Collections.reverse(list);
        list.forEach(action);
    }

    public boolean isNotEmpty() {
        return CollectionUtils.isNotEmpty(set);
    }
}
