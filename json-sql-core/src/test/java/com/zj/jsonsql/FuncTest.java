package com.zj.jsonsql;

import com.zj.jsonsql.exception.SqlException;
import com.zj.jsonsql.utils.SqlTestUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author : jie.zhou
 * @date : 2025/5/22
 */
public class FuncTest {

    // id       name    code            parent_id   level   detail:{code    name}   children:[{code name}]
    // 867-988          320000-321324   1-982       1-2

    @Test
    public void test1() {
        SqlTestUtil.out("select left(right(left(id,3),2), 1) a, count( left(right(left(id,3),2), 1)) b from t_user group by a having b order by b");
    }

    @Test
    public void test2() {
        SqlTestUtil.out("select *, left(right(left(id,3),2), 1) a, count( left(right(left(id,3),2), 1)) b from t_user group by a having b order by b");
    }

    @Test
    public void testIf3() {
        SqlTestUtil.out("select id, if(1=1, id, null), ifnull(if(id > 905, id, null), '123'), nullif(id,if(id <= 905, id, null)) from t_user where id between 900 and 910");
    }

    @Test
    public void testError4() {
        Assert.assertThrows("sql应该语句错误", SqlException.class,
                () -> SqlTestUtil.out("select if() from arr limit 5")
        ).printStackTrace();
        Assert.assertThrows("sql应该语句错误", SqlException.class,
                () -> SqlTestUtil.out("select if(1,2,3, 3) from arr limit 5")
        ).printStackTrace();
        Assert.assertThrows("sql应该语句错误", Exception.class,
                () -> SqlTestUtil.out("select if(*,3,1) from arr limit 5")
        ).printStackTrace();
        Assert.assertThrows("sql应该语句错误", SqlException.class,
                () -> SqlTestUtil.out("select isnull(`*`) from arr limit 5")
        ).printStackTrace();
    }
}
