package com.perswsj.memberweb.servlet;

import com.perswsj.memberweb.exception.DuplicateUsernameException;
import com.perswsj.memberweb.dao.JdbcMemberDao;
import com.perswsj.memberweb.dao.MemberDao;
import com.perswsj.memberweb.pojo.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 新增会员：GET 显示表单，POST 校验并写库，完成后重定向回列表页（PRG，避免刷新重复提交）。
 */
@WebServlet("/member-add")
public class MemberAddServlet extends HttpServlet {

    private static final String LIST_URL = "member-list";

    private final MemberDao memberDao = new JdbcMemberDao();

    /** GET：显示新增表单 */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/add.jsp").forward(req, resp);
    }

    /** POST：接收并保存表单数据 */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 必须在读取任何参数之前设置编码，否则中文乱码
        req.setCharacterEncoding("UTF-8");

        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");
        String email = trim(req.getParameter("email"));
        String phone = trim(req.getParameter("phone"));
        String gender = req.getParameter("gender");
        String hobby = trim(req.getParameter("hobby"));
        String remark = trim(req.getParameter("remark"));

        if (username.isEmpty()) {
            redirectWithMessage(resp, "新增失败：用户名不能为空");
            return;
        }
        if (password == null || password.isEmpty()) {
            redirectWithMessage(resp, "新增失败：密码不能为空");
            return;
        }

        Member member = new Member();
        member.setUname(username);
        member.setPassword(password);
        member.setEmail(email);
        member.setPhone(phone);
        member.setSex(gender);
        member.setHobby(hobby);
        member.setRemark(remark);

        try {
            int rows = memberDao.add(member);
            if (rows > 0) {
                redirectWithMessage(resp, "新增成功");
            } else {
                // 未抛异常但影响 0 行，同样算失败
                redirectWithMessage(resp, "新增失败，请重试");
            }
        } catch (DuplicateUsernameException e) {
            log("新增会员用户名重复：" + username);
            redirectWithMessage(resp, "用户名已存在");
        } catch (RuntimeException e) {
            log("新增会员失败，username = " + username, e);
            redirectWithMessage(resp, "新增失败，请稍后重试");
        }
    }

    /** 消息含中文，必须 URL 编码，否则浏览器与 Tomcat 解码不一致会乱码 */
    private void redirectWithMessage(HttpServletResponse resp, String message) throws IOException {
        String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8);
        resp.sendRedirect(LIST_URL + "?message=" + encoded);
    }

    /** null 视为空串，避免把 null 写进数据库 */
    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
