package com.zj.demoplugin.utils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author arthur_zhou
 */
public class CompareUtil {
    /**
     * 比较两个对象,如果都是Number，就用数字比较，否则用字符串比较
     *
     * @param var1
     * @param var2
     * @return <0:var小 0:相同 >0:var1大
     */
    public static int compare(Object var1, Object var2) {
        if (Objects.isNull(var1) && Objects.isNull(var2)) {
            return 0;
        }
        if (Objects.isNull(var1)) {
            return -1;
        }
        if (Objects.isNull(var2)) {
            return 1;
        }
        if (var1 instanceof Number && var2 instanceof Number) {
            return new BigDecimal(var1.toString()).compareTo(new BigDecimal(var2.toString()));
        }
        return var1.toString().compareTo(var2.toString());
    }

}
