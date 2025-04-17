package com.zj.jsonsql;

import com.zj.jsonsql.utils.SqlTestUtil;
import org.junit.Test;

public class SqlTest {

    //id		code		name		parentId		parentName		groupName		isChoosed		isDisabled		childList		children
    //2484		510100		成都市		2483		四川省		华西		1		NULL		NULL		NULL

    /**
     * 函数测试
     */
    @Test
    public void test() {
        SqlTestUtil.out("select left(code, 4) a,right(123,2) n,if(right(123,2),code,null) from t_user where left(code, 4) = '5101'");
    }

    @Test
    public void ifTest() {
        SqlTestUtil.out("select if(left(left('510100',6), 4) = '5101',left(left('510100',6), 4) = '5101',0) a,if(code,1,0) b,if(right(123,2),1,0)c from t_user where left(code, 4) = '5101'");
    }

    @Test
    public void simpleIfTest() {
        SqlTestUtil.out("select 1 a,if(code,1,0) b,if(right(123,2),1,0)c from t_user where left(code, 4) = '5101'");
    }

    @Test
    public void isNullTest() {
        SqlTestUtil.out("select * from t_user where isnull(`children`)");
    }

    @Test
    public void funcTest11111() {
        SqlTestUtil.out("select *  from t_user where right(left(id,3),1) = right(left(parentId,3),1)");
        SqlTestUtil.out("select *  from t_user where name like concat('__','市')");
    }

    @Test
    public void orderTest() {
        SqlTestUtil.out("select left(right(left(id,3),2), 1) b, right(left(id,3),2) a from t_user order by left(a, 1) asc, a ");
    }

    @Test
    public void groupTest() {
        SqlTestUtil.out("select left(right(left(id,3),2), 1) a, count( left(right(left(id,3),2), 1)) b from t_user group by a ");
    }

    @Test
    public void groupTest1() {
        SqlTestUtil.out("select id a,left(min(id), 1) from t_user group by left(v, 10)");
    }

}
