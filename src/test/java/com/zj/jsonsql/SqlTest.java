package com.zj.jsonsql;

import org.junit.Test;

public class SqlTest {

    //id		code		name		parentId		parentName		groupName		isChoosed		isDisabled		childList		children
    //2484		510100		成都市		2483		四川省		华西		1		NULL		NULL		NULL
    @Test
    public void test() {
        SqlTestUtil.out("select left(code, 4) from t_user where left(code, 4) = '5101'");
    }

}
