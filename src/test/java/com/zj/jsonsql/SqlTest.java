package com.zj.jsonsql;

import org.junit.Test;

public class SqlTest {

    //id		code		name		parentId		parentName		groupName		isChoosed		isDisabled		childList		children
    //2484		510100		成都市		2483		四川省		华西		1		NULL		NULL		NULL

    /**
     * 函数测试
     */
    @Test
    public void test() {
        SqlTestUtil.out("select left(code, 4) a,count(*) b,right(123,2) n,if(right(123,2),code,null) from t_user where left(code, 4) = '5101' group by id");
    }

    @Test
    public void ifTest() {
        SqlTestUtil.out("select if(left(left('510100',6), 4) = '5101',left(left('510100',6), 4) = '5101',0) a,if(code,1,0) b,if(right(123,2),1,0)c from t_user where left(code, 4) = '5101' group by id");
    }

    @Test
    public void simpleIfTest() {
        SqlTestUtil.out("select 1 a,if(code,1,0) b,if(right(123,2),1,0)c from t_user where left(code, 4) = '5101' group by id");
    }

}
