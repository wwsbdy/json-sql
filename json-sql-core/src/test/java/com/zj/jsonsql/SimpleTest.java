package com.zj.jsonsql;

import com.zj.jsonsql.exception.SqlException;
import com.zj.jsonsql.utils.SqlTestUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author : jie.zhou
 * @date : 2025/5/22
 */
public class SimpleTest {

    // id       name    code            parent_id   level   detail:{code    name}   children:[{code name}]
    // 867-988          320000-321324   1-982       1-2

    @Test
    public void test1() {
        SqlTestUtil.out("select id from arr limit 5");
    }

    @Test
    public void test2() {
        SqlTestUtil.out("select * from arr limit 5");
    }

    @Test
    public void testLike3() {
        SqlTestUtil.out("select * from arr where id > 870 and code like '32__00' limit 5");
    }

    @Test
    public void testLike4() {
        SqlTestUtil.out("select * from arr where code like '3201%' limit 5");
    }

    @Test
    public void testEquals4() {
        SqlTestUtil.out("select * from arr where id = '910' and code = '320500'");
    }

    @Test
    public void testIn5() {
        SqlTestUtil.out("select * from arr where id in (867, 988)");
    }

    @Test
    public void testInnerAndOr5() {
        SqlTestUtil.out("select id, name, detail, detail.code from arr where id = '910' and code = '320500' or id in (867, 988)");
    }

    @Test
    public void testRange6() {
        SqlTestUtil.out("select id from arr where id >= '910' limit 5");
    }

    @Test
    public void testRange7() {
        SqlTestUtil.out("select id from arr where id < '910' order by id desc limit 5");
    }

    @Test
    public void testRange8() {
        SqlTestUtil.out("select id from arr where id <= '910' order by id desc limit 5");
    }

    @Test
    public void testRange9() {
        SqlTestUtil.out("select id from arr where id between 900 and 903 order by id desc limit 5");
    }

    @Test
    public void testRange10() {
        SqlTestUtil.out("select id from arr where id between 900 and 903 or id < 906 and id >= 902 order by id desc limit 5");
    }

    @Test
    public void testNull11() {
        SqlTestUtil.out("select id, level is null, level from arr where 1 = 1 limit 5");
    }

    @Test
    public void testExist12() {
        Assert.assertThrows("a字段应该不存在", SqlException.class,
                () -> SqlTestUtil.out("select id, a from arr limit 5")
        ).printStackTrace();
    }

    @Test
    public void testError13() {
        Assert.assertThrows("sql应该语句错误", Exception.class,
                () -> SqlTestUtil.out("select id from arr a=1 limit 5")
        ).printStackTrace();
    }

    @Test
    public void testError14() {
        Assert.assertThrows("sql应该语句错误", Exception.class,
                () -> SqlTestUtil.out("select id a from arr where a=1 limit 5")
        ).printStackTrace();
    }

    @Test
    public void testAlias15() {
        SqlTestUtil.out("select id a,if(id between 900 and 902, 1, 0) b from arr where id between 900 and 910 having a > 10 order by b desc, id");
    }
    @Test
    public void testFourFundamentalRules() {
        SqlTestUtil.out("select id, id + '10' / 10 a, id / 10 + 10 b, id + (10 / 10) c from t_user limit 4");
    }

}
