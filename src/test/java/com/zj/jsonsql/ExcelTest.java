package com.zj.jsonsql;

import com.zj.jsonsql.utils.EasyExcelUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * @author : jie.zhou
 * @date : 2025/4/18
 */
public class ExcelTest {

    @Test
    public void test() throws IOException {
        String filePath = "/Users/jie.zhou/Desktop";
        EasyExcelUtil.read(filePath);
    }
}
