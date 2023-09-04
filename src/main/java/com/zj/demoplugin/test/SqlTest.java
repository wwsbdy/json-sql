package com.zj.demoplugin.test;

import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;

/**
 * @author arthur_zhou
 */
public class SqlTest {

    public void myTest(){

    }


    public static boolean validateSQL(String sql) {
        try {
            SqlParser.Config config = SqlParser.config()
                    .withLex(Lex.MYSQL)
                    // 保持原有大小写
                    .withCaseSensitive(false);
            SqlParser parser = SqlParser.create(sql, config);
            SqlNode node = parser.parseStmt();
            SqlKind kind = node.getKind();
            if (SqlKind.SELECT == kind) {
                SqlSelect sqlSelect = (SqlSelect) node;
                for (SqlNode sqlNode : sqlSelect.getSelectList()) {
                    SqlKind kind1 = sqlNode.getKind();
                    System.out.println(sqlNode.toString());
                }
            }
            return true;
        } catch (SqlParseException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String sql2 = "SELECT *,a(A) tad,b,c FROM mytable WHERE id = '1' or i = r and c=b and (1=1 or 2=2)";

        System.out.println(validateSQL(sql2)); // true
    }
}
