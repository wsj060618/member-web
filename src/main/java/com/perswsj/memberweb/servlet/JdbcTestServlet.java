package com.perswsj.memberweb.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 临时连接测试 Servlet：验证 "JNDI 查找 -> 连接池 -> PreparedStatement -> ResultSet" 整条链路。
 * 验证通过后可删除；正式代码里的数据源查找由 DataSourceUtil 统一承担。
 * 访问地址：http://localhost:8080/member-web/jdbcTest
 */
@WebServlet("/jdbcTest")
public class JdbcTestServlet extends HttpServlet {

    /** 只含一个问号占位符：按主键查一条会员 */
    private static final String SQL =
            "SELECT id, username, email, phone, gender, hobby FROM member WHERE id = ?";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'>"
                + "<title>JDBC 连接测试</title></head><body>");
        out.println("<h2>JNDI 数据源连接测试</h2>");

        final int id = 1;

        // 通过 InitialContext 查找 java:comp/env/jdbc/memberDS，拿到 DataSource
        DataSource ds;
        try {
            InitialContext ic = new InitialContext();
            ds = (DataSource) ic.lookup("java:comp/env/jdbc/memberDS");
        } catch (NamingException e) {
            // 查找失败属于配置问题，异常只进日志，页面只给提示
            log("查找数据源失败：请检查 META-INF/context.xml 是否随应用部署、"
                    + "mysql-connector-j 是否在 Tomcat 的 lib 目录", e);
            out.println("<p style='color:red'>查找数据源失败，请查看服务器日志</p></body></html>");
            return;
        }

        // 调用 getConnection 取连接
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            // 绑定参数，下标从 1 开始，顺序与问号顺序一致
            ps.setInt(1, id);

            // 执行查询，ResultSet 也交给 try-with-resources 管理
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                out.println("<table border='1' cellpadding='6' cellspacing='0'>");
                out.println("<tr><th>编号</th><th>用户名</th><th>邮箱</th>"
                        + "<th>手机</th><th>性别</th><th>爱好</th></tr>");
                // 遍历结果集，把每行字段组织成 HTML 表格输出
                while (rs.next()) {
                    found = true;
                    String rawGender = rs.getString("gender");
                    String gender = ("M".equalsIgnoreCase(rawGender))
                            ? "男" : "女";
                    out.println("<tr>"
                            + "<td>" + rs.getInt("id") + "</td>"
                            + "<td>" + rs.getString("username") + "</td>"
                            + "<td>" + rs.getString("email") + "</td>"
                            + "<td>" + rs.getString("phone") + "</td>"
                            + "<td>" + gender + "</td>"
                            + "<td>" + rs.getString("hobby") + "</td>"
                            + "</tr>");
                }
                out.println("</table>");
                if (found) {
                    out.println("<p>链路 JNDI → DataSource → Connection → PreparedStatement → ResultSet 全部打通。</p>");
                } else {
                    out.println("<p>未查询到 id = " + id + " 的会员记录。</p>");
                }
            }
        } catch (SQLException e) {
            // SQL 异常只在控制台记录，页面不输出异常原文（避免泄露 SQL、库名、账号）
            log("查询会员数据失败，id = " + id, e);
            out.println("<p style='color:red'>查询失败，请查看服务器日志</p>");
        }

        out.println("</body></html>");
    }
}
