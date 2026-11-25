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
 * 修改会员控制层：分"查后改"两步完成。
 * <p>
 * GET 请求带上 id 进来，先复用 DAO 的 {@code findById} 从数据库查出原记录，回显到 edit.jsp；
 * POST 请求接收表单提交，组装 Member 后调用 {@code update} 写回数据库，
 * 按受影响行数判断成败，最后统一重定向回列表页（PRG 模式，避免刷新导致重复提交）。
 * <p>
 * 职责边界：本类只做参数接收与校验、结果提示，SQL 与参数绑定全部在 {@link JdbcMemberDao} 内完成。
 */
@WebServlet("/member-update")
public class MemberUpdateServlet extends HttpServlet {

    private static final String LIST_URL = "member-list";

    /** 无状态 DAO 可作为成员变量共享；Connection 等资源不放在这里 */
    private final MemberDao memberDao = new JdbcMemberDao();

    /**
     * GET：按 id 查出原记录，转发到编辑页回显。
     * <p>
     * 查不到（id 缺失、格式非法、记录已被删除）时不进入表单，而是提示后重定向回列表页。
     */
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
            // SQL 异常
            log("查询待修改会员失败，id = " + id, e);
            redirectWithMessage(resp, "查询会员失败，请稍后重试");
            return;
        }

        if (member == null) {
            redirectWithMessage(resp, "会员不存在或已被删除");
            return;
        }

        // 存入 request 域，供 edit.jsp 用 EL 回显各个输入框的原值
        req.setAttribute("member", member);
        req.getRequestDispatcher("/edit.jsp").forward(req, resp);
    }

    /** POST：接收表单数据并更新记录 */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 必须在第一次读取参数之前设置编码，否则中文会乱码
        req.setCharacterEncoding("UTF-8");

        // 1. 读取隐藏域中的 id；缺失或非法时不必继续，直接提示
        Integer id = parseId(req.getParameter("id"));
        if (id == null) {
            redirectWithMessage(resp, "修改失败：编号不存在或数据未变化");
            return;
        }

        // 2. 读取七个表单参数，组装成 Member 对象
        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");
        String email = trim(req.getParameter("email"));
        String phone = trim(req.getParameter("phone"));
        String gender = req.getParameter("gender");
        String hobby = trim(req.getParameter("hobby"));
        String remark = trim(req.getParameter("remark"));

        // 3. 校验必填项；不合法时携带错误提示重定向回列表页
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
        // 表单提交的是 M/F，DAO 写库前统一处理
        member.setSex(gender);
        member.setHobby(hobby);
        member.setRemark(remark);

        // 4. 调用 DAO 更新，并检查受影响行数
        try {
            int rows = memberDao.update(member);
            if (rows > 0) {
                redirectWithMessage(resp, "修改成功");
            } else {
                // 返回 0：id 不存在，或提交的数据与库中原值完全相同
                redirectWithMessage(resp, "修改失败：编号不存在或数据未变化");
            }
        } catch (DuplicateUsernameException e) {
            // 把用户名改成了其他会员已占用的名字
            log("修改会员用户名重复：" + username + "，id = " + id);
            redirectWithMessage(resp, "用户名已存在");
        } catch (RuntimeException e) {
            log("修改会员失败，id = " + id + "，username = " + username, e);
            redirectWithMessage(resp, "修改失败，请稍后重试");
        }
    }

    /**
     * 把请求参数解析成主键。
     * <p>
     * 参数缺失、空串、非数字、非正数一律返回 null，交由调用方给出统一提示，
     * 避免 {@code Integer.parseInt} 抛 NumberFormatException 导致 500 错误页。
     */
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
