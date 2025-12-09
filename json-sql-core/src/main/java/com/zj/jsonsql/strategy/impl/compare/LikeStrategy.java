package com.zj.jsonsql.strategy.impl.compare;

import com.zj.jsonsql.entity.Row;
import com.zj.jsonsql.strategy.AbstractWhereStrategy;
import com.zj.jsonsql.utils.JsonUtil;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 模糊查询
 *
 * @author arthur_zhou
 */
public class LikeStrategy extends AbstractWhereStrategy {

    private SqlNode value;

    public static final String LIKE = "LIKE";
    public static final String NOT_LIKE = "NOT LIKE";

    public LikeStrategy(boolean reverse, List<SqlNode> operandList) {
        super(reverse);
        if (CollectionUtils.isEmpty(operandList) || operandList.size() != SIMPLE_SIZE) {
            return;
        }
        setField(operandList.get(0));
        this.value = operandList.get(1);
    }

    public LikeStrategy(SqlBasicCall where) {
        this(LikeStrategy.NOT_LIKE.equalsIgnoreCase(String.valueOf(where.getOperator())), where.getOperandList());
    }

    @Override
    public boolean apply(Row item) {
        if (Objects.isNull(item) || Objects.isNull(getField()) || Objects.isNull(value)) {
            return false;
        }
        boolean equals = false;
        Object o = item.get(getField());
        Object convert = JsonUtil.convert(o);
        String compareValue = String.valueOf(item.get(value));
        // 如果是数组，只要有一个满足就行
        if (convert instanceof List) {
            List<?> list = (List<?>) convert;
            for (Object o1 : list) {
                if (like(String.valueOf(o1), compareValue)) {
                    equals = true;
                    break;
                }
            }
        } else {
            equals = like(String.valueOf(convert), compareValue);
        }
        return isReverse() != equals;
    }

    public static boolean like(String text, String likePattern) {
        StringBuilder regex = new StringBuilder();
        boolean escape = false;
        for (int i = 0; i < likePattern.length(); i++) {
            char c = likePattern.charAt(i);
            // 上一个字符是 \
            if (escape) {
                // 如果是 % 或 _ ，它们应被当作普通字符
                if (c == '%' || c == '_' || c == '\\') {
                    // 转成正则安全字符
                    regex.append("\\").append(c);
                } else {
                    // 其他字符：\x 的含义就是 x 字符本身
                    if ("\\.[]{}()*+-?^$|".indexOf(c) != -1) {
                        regex.append("\\");
                    }
                    regex.append(c);
                }
                escape = false;
                continue;
            }
            if (c == '\\') {
                escape = true;
                continue;
            }
            switch (c) {
                case '%':
                    // 匹配任意长度字符（含换行）
                    regex.append("[\\s\\S]*");
                    break;
                case '_':
                    // 匹配一个字符（含换行）
                    regex.append("[\\s\\S]");
                    break;
                default:
                    // 处理正则特殊字符
                    if ("\\.[]{}()*+-?^$|".indexOf(c) != -1) {
                        regex.append("\\");
                    }
                    regex.append(c);
            }
        }
        if (escape) {
            // pattern 以 \ 结尾 (MySQL: 最后的 \ 当作普通字符)
            regex.append("\\\\");
        }
        return text.matches("^" + regex + "$");
    }
}
