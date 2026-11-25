package com.perswsj.memberweb.servlet;

import com.perswsj.memberweb.dao.JdbcMemberDao;
import com.perswsj.memberweb.dao.MemberDao;
import com.perswsj.memberweb.exception.DuplicateUsernameException;
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
 * 修改会员：GET 按 id 查原记录回显到 edit.jsp，POST 更新并重定向回列表页（PRG）。
 */
@WebServlet("/member-update")
public class MemberUpdateServlet extends HttpServlet {

    private static final String LIST_URL = "member-list";

    private final MemberDao memberDao = new JdbcMemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = parseId(req.getParameter("id"));
        if (id == null) {
            redirectWithMessage(resp, "会员不存在或已被删除");
            return;
        }

        Member member;
        try {
            member = memberDao.findById(id);
        } catch (RuntimeException e) {
            log("查询待修改会员失败，id = " + id, e);
            redirectWithMessage(resp, "查询会员失败，请稍后重试");
            return;
        }

        if (member == null) {
            redirectWithMessage(resp, "会员不存在或已被删除");
            return;
        }

        req.setAttribute("member", member);
        req.getRequestDispatcher("/edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 必须在读取任何参数之前设置编码，否则中文乱码
        req.setCharacterEncoding("UTF-8");

        Integer id = parseId(req.getParameter("id"));
        if (id == null) {
            redirectWithMessage(resp, "修改失败：编号不存在或数据未变化");
            return;
        }

        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");
        String email = trim(req.getParameter("email"));
        String phone = trim(req.getParameter("phone"));
        String gender = req.getParameter("gender");
        String hobby = trim(req.getParameter("hobby"));
        String remark = trim(req.getParameter("remark"));

        if (username.isEmpty()) {
            redirectWithMessage(resp, "修改失败：用户名不能为空");
            return;
        }
        if (password == null || password.isEmpty()) {
            redirectWithMessage(resp, "修改失败：密码不能为空");
            return;
        }

        Member member = new Member();
        member.setId(id);
        member.setUname(username);
        member.setPassword(password);
        member.setEmail(email);
        member.setPhone(phone);
        member.setSex(gender);
        member.setHobby(hobby);
        member.setRemark(remark);

        try {
            int rows = memberDao.update(member);
            if (rows > 0) {
                redirectWithMessage(resp, "修改成功");
            } else {
                // 影响 0 行：id 不存在，或数据与库中原值完全相同
                redirectWithMessage(resp, "修改失败：编号不存在或数据未变化");
            }
        } catch (DuplicateUsernameException e) {
            log("修改会员用户名重复：" + username + "，id = " + id);
            redirectWithMessage(resp, "用户名已存在");
        } catch (RuntimeException e) {
            log("修改会员失败，id = " + id + "，username = " + username, e);
            redirectWithMessage(resp, "修改失败，请稍后重试");
        }
    }

    /** 参数缺失、非数字、非正数一律返回 null，避免 parseInt 抛异常导致 500。 */
    private Integer parseId(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            int id = Integer.parseInt(raw.trim());
            return id > 0 ? id : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** PRG 重定向。消息含中文，必须 URL 编码，否则浏览器与 Tomcat 解码不一致会乱码。 */
    private void redirectWithMessage(HttpServletResponse resp, String message) throws IOException {
        String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8);
        resp.sendRedirect(LIST_URL + "?message=" + encoded);
    }

    /** null 视为空串，避免把 null 写进数据库 */
    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
