package com.zj.jsonsql;

import com.zj.jsonsql.exception.SqlException;
import com.zj.jsonsql.utils.SqlTestUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author : jie.zhou
 * @date : 2025/5/22
 */
public class GroupTest {

    // id       name    code            parent_id   level   detail:{code    name}   children:[{code name}]
    // 867-988          320000-321324   1-982       1-2

    @Test
    public void test1() {
        SqlTestUtil.out("select parent_id,max(code),min(code),avg(code),group_array(code) from arr group by parent_id");
    }

    @Test
    public void test2() {
        SqlTestUtil.out("select max(code),min(code),avg(code),group_array(code) from arr");
    }

    @Test
    public void test3() {
        SqlTestUtil.out("select parent_id pid, group_concat(code),group_array(code),group_array(id) from arr where id = '910' and code = '320500' or id in (867, 988) group by pid");
    }
    @Test
    public void test4() {
        SqlTestUtil.out("select left(right(left(id,3),2), 1) a, count( left(right(left(id,3),2), 1)) b,group_concat(name) from t_user where left(right(left(id,3),2), 1) <= 5 group by a ");
    }
    @Test
    public void test5() {
        Assert.assertThrows("group_array应该语句错误", SqlException.class,
                () -> SqlTestUtil.out("select parent_id,group_array(*) from arr where parent_id between 900 and 921 group by parent_id")
        ).printStackTrace();
    }
    @Test
    public void test6() {
        SqlTestUtil.out("select parent_id pid, group_concat(detail.a),group_array(detail.a) from arr where id = '910' and code = '320500' or id in (867, 988) group by pid");
    }
    @Test
    public void testCount7() {
        SqlTestUtil.out("select count(*), count(level), count(id, level), group_concat(level), group_array(level), group_concat(ifnull(level, 'Null2')) from arr where id in (867, 868, 879, 880)");
    }
}
