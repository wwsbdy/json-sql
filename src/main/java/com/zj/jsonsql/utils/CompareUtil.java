package com.zj.jsonsql.utils;

import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.util.Objects;

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
        String str1 = var1.toString();
        String str2 = var2.toString();
        if (NumberUtils.isCreatable(str1) && NumberUtils.isCreatable(str2)) {
            return new BigDecimal(str1).compareTo(new BigDecimal(str2));
        }
        return str1.compareTo(str2);
    }

}
