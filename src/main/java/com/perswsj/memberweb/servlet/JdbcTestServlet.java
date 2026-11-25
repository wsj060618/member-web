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
 * 临时验证 JNDI → 连接池 → PreparedStatement → ResultSet 整条链路，验证通过后可删。
 * 访问地址：http://localhost:8080/member-web/jdbcTest
 */
@WebServlet("/jdbcTest")
public class JdbcTestServlet extends HttpServlet {

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

        DataSource ds;
        try {
            ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/memberDS");
        } catch (NamingException e) {
            log("查找数据源失败：请检查 META-INF/context.xml 是否随应用部署、"
                    + "mysql-connector-j 是否在 Tomcat 的 lib 目录", e);
            out.println("<p style='color:red'>查找数据源失败，请查看服务器日志</p></body></html>");
            return;
        }

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                out.println("<table border='1' cellpadding='6' cellspacing='0'>");
                out.println("<tr><th>编号</th><th>用户名</th><th>邮箱</th>"
                        + "<th>手机</th><th>性别</th><th>爱好</th></tr>");
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
            // 异常原文会带出 SQL、库名、账号，只进日志
            log("查询会员数据失败，id = " + id, e);
            out.println("<p style='color:red'>查询失败，请查看服务器日志</p>");
        }

        out.println("</body></html>");
    }
}
