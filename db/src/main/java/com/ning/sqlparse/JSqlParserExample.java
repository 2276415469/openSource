package com.ning.sqlparse;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.util.List;

public class JSqlParserExample {
    //可以解析sql为AST树
//    可以修改sql通过set
//    可以自己创建sql通过创建table column where条件等
//    通过生成的ast对象可以做安全校验、可以做操作限制如不允许drop、还可以做表权限的处理
    public static void main(String[] args) throws Exception {
        String sql = "SELECT u.name, (SELECT COUNT(*) FROM orders o WHERE o.user_id = u.id) AS order_count "
                + "FROM users u "
                + "WHERE u.id IN (SELECT user_id FROM active_users WHERE status = 'ACTIVE')";

        Statement statement = CCJSqlParserUtil.parse(sql);
        if (statement instanceof Select) {
            Select select = (Select) statement;
            SelectBody selectBody = select.getSelectBody();
            processSelectBody(selectBody, 0); // 从嵌套层级 0 开始
        }
    }

    private static void processSelectBody(SelectBody selectBody, int level) {
        if (selectBody instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) selectBody;
            // 输出当前层级的 SELECT
            System.out.println(indent(level) + "SELECT层级: " + level);

            // 处理子查询（嵌套 SELECT）
            for (SelectItem item : plainSelect.getSelectItems()) {
                if (item instanceof SelectExpressionItem) {
                    SelectExpressionItem exprItem = (SelectExpressionItem) item;
                    if (exprItem.getExpression() instanceof SubSelect) {
                        System.out.println(indent(level) + "发现子查询:");
                        SubSelect subSelect = (SubSelect) exprItem.getExpression();
                        processSelectBody(subSelect.getSelectBody(), level + 1); // 递归处理子查询
                    }
                }
            }

            // 处理 WHERE 子句中的子查询
            if (plainSelect.getWhere() != null) {
                plainSelect.getWhere().accept(new ExpressionVisitorAdapter() {
                    @Override
                    public void visit(SubSelect subSelect) {
                        System.out.println(indent(level) + "WHERE子句中的子查询:");
                        processSelectBody(subSelect.getSelectBody(), level + 1);
                    }
                });
            }

        } else if (selectBody instanceof SetOperationList) {
            // 处理 UNION/INTERSECT 等集合操作
            SetOperationList setOpList = (SetOperationList) selectBody;
            for (SelectBody body : setOpList.getSelects()) {
                processSelectBody(body, level + 1);
            }
        }
    }

    private static String indent(int level) {
        String result="";
        for (int i = 0; i < level; i++) {
            result+=" ";
        }
        return result;
    }
}
