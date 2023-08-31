package com.zj.demoplugin.utils;

import java.util.Collection;

/**
 * @author arthur_zhou
 */
public class CollectionUtil {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return false == isEmpty(collection);
    }
}
