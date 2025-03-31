package com.zj.jsonsql.strategy.impl.func;

import com.zj.jsonsql.enums.FuncEnum;

/**
 * @author : jie.zhou
 * @date : 2025/3/31
 */
public class SubstrStrategy extends SubstringStrategy {

    @Override
    public FuncEnum getType() {
        return FuncEnum.SUBSTR;
    }
}
