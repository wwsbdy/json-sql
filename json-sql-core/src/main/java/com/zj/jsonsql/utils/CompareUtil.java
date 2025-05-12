package com.zj.jsonsql.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author arthur_zhou
 */
public class CompareUtil {
    /**
     * 比较两个对象,如果都是Number，就用数字比较，否则用字符串比较
     *
     * @param var1 值1
     * @param var2 值2
     * @return <0:var小 0:相同 >0:var1大
     */
    public static int compare(Object var1, Object var2) {
        if (Objects.isNull(var1)) {
            return Objects.isNull(var2) ? 0 : -1;
        }
        if (Objects.isNull(var2)) {
            return 1;
        }
        // 如果是集合
        if (var1 instanceof Collection && var2 instanceof Collection) {
            return compareCollection(((Collection<?>) var1), ((Collection<?>) var2));
        } else if (var1 instanceof Map) {
            return compareCollection(((Map<?, ?>) var1).entrySet(), ((Map<?, ?>) var2).entrySet());
        }
        String str1 = var1.toString();
        String str2 = var2.toString();
        if (NumberUtils.isCreatable(str1) && NumberUtils.isCreatable(str2)) {
            return new BigDecimal(str1).compareTo(new BigDecimal(str2));
        }
        return str1.compareTo(str2);
    }

    /**
     * 比较两个集合,
     * 长度不相等时，返回长度差，否则比较集合内元素
     *
     * @param var1 值1
     * @param var2 值2
     * @return <0:var小 0:相同 >0:var1大
     */
    public static int compareCollection(Collection<?> var1, Collection<?> var2) {
        if (CollectionUtils.isEmpty(var1)) {
            return CollectionUtils.isEmpty(var2) ? 0 : -1;
        }
        if (CollectionUtils.isEmpty(var2)) {
            return 1;
        }
        if (var1.size() != var2.size()) {
            return var1.size() - var2.size();
        }
        List<?> list1 = new ArrayList<>(var1);
        List<?> list2 = new ArrayList<>(var2);
        for (Object o : var1) {
            list2.remove(o);
        }
        for (Object o : var2) {
            list1.remove(o);
        }
        return list1.size() - list2.size();
    }

    /**
     * 判断值是否为true
     *
     * @param val 值
     * @return true:是
     */
    public static boolean isRight(Object val) {
        return Objects.nonNull(val)
                && (!(val instanceof Boolean) || (Boolean) val)
                && !"0".equals(val.toString());
    }

}
