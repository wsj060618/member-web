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
 * 新增会员控制层。
 * <p>
 * GET 请求转发到 add.jsp 显示表单；POST 请求接收表单数据，校验后调用 DAO 的 add 方法写入 member 表，
 * 按受影响行数判断成败，最后统一重定向回列表页（PRG 模式，避免刷新导致重复提交）。
 * <p>
 * 职责边界：本类只做参数接收与校验、结果提示，SQL 与参数绑定全部在 {@link JdbcMemberDao} 内完成。
 */
@WebServlet("/member-add")
public class MemberAddServlet extends HttpServlet {

    private static final String LIST_URL = "member-list";

    /** 无状态 DAO 可作为成员变量共享；Connection 等资源不放在这里 */
    private final MemberDao memberDao = new JdbcMemberDao();

    /** GET：显示新增表单 */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/add.jsp").forward(req, resp);
    }

    /** POST：接收并保存表单数据 */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 必须在第一次读取参数之前设置编码，否则中文会乱码
        req.setCharacterEncoding("UTF-8");

        // 1. 读取七个表单参数，组装成 Member 对象
        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");
        String email = trim(req.getParameter("email"));
        String phone = trim(req.getParameter("phone"));
        String gender = req.getParameter("gender");
        String hobby = trim(req.getParameter("hobby"));
        String remark = trim(req.getParameter("remark"));

        // 2. 校验用户名非空；不合法时携带错误提示重定向回列表页
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
        // 表单提交的是 male/female，DAO 写库前会转成 M/F
        member.setSex(gender);
        member.setHobby(hobby);
        member.setRemark(remark);

        // 3. 调用 DAO 写入，并检查受影响行数
        try {
            int rows = memberDao.add(member);
            if (rows > 0) {
                redirectWithMessage(resp, "新增成功");
            } else {
                // 没抛异常但一行都没插进去，同样算失败
                redirectWithMessage(resp, "新增失败，请重试");
            }
        } catch (DuplicateUsernameException e) {
            // 唯一索引冲突：属于可预期的业务失败，给出明确提示
            log("新增会员用户名重复：" + username);
            redirectWithMessage(resp, "用户名已存在");
        } catch (RuntimeException e) {
            // 其他 SQL 异常只在控制台留日志，页面不输出异常原文（避免泄露 SQL、库名、账号）
            log("新增会员失败，username = " + username, e);
            redirectWithMessage(resp, "新增失败，请稍后重试");
        }
    }

    /**
     * PRG：重定向回列表页并携带提示消息。
     * 提示消息含中文，必须经过 URL 编码，否则浏览器与 Tomcat 解码不一致会出现乱码。
     */
    private void redirectWithMessage(HttpServletResponse resp, String message) throws IOException {
        String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8);
        resp.sendRedirect(LIST_URL + "?message=" + encoded);
    }

    /** 去首尾空格，null 视为空串，避免把 null 写进数据库 */
    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
