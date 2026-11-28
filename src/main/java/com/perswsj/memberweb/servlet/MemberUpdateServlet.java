package com.perswsj.memberweb.servlet;

import com.perswsj.memberweb.dao.JdbcMemberDao;
import com.perswsj.memberweb.dao.MemberDao;
import com.perswsj.memberweb.exception.DuplicateUsernameException;
import com.perswsj.memberweb.pojo.Member;
import com.perswsj.memberweb.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 修改会员：GET 按 id 查原记录回显到 edit.jsp，POST 更新并重定向回列表页（PRG）。
 */
@WebServlet("/member-update")
public class MemberUpdateServlet extends HttpServlet {

    private final MemberDao memberDao = new JdbcMemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = WebUtil.parseId(req.getParameter("id"));
        if (id == null) {
            WebUtil.redirectToList(resp, "会员不存在或已被删除");
            return;
        }

        Member member;
        try {
            member = memberDao.findById(id);
        } catch (RuntimeException e) {
            log("查询待修改会员失败，id = " + id, e);
            WebUtil.redirectToList(resp, "查询会员失败，请稍后重试");
            return;
        }

        if (member == null) {
            WebUtil.redirectToList(resp, "会员不存在或已被删除");
            return;
        }

        req.setAttribute("member", member);
        req.getRequestDispatcher("/edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 必须在读取任何参数之前设置编码，否则中文乱码
        req.setCharacterEncoding("UTF-8");

        Integer id = WebUtil.parseId(req.getParameter("id"));
        if (id == null) {
            WebUtil.redirectToList(resp, "修改失败：编号不存在或数据未变化");
            return;
        }

        Member member = MemberForm.fromRequest(req, id);

        String error = MemberForm.validate(member);
        if (error != null) {
            WebUtil.redirectToList(resp, "修改失败：" + error);
            return;
        }

        try {
            int rows = memberDao.update(member);
            if (rows > 0) {
                WebUtil.redirectToList(resp, "修改成功");
            } else {
                // 影响 0 行：id 不存在，或数据与库中原值完全相同
                WebUtil.redirectToList(resp, "修改失败：编号不存在或数据未变化");
            }
        } catch (DuplicateUsernameException e) {
            log("修改会员用户名重复：" + member.getUname() + "，id = " + id);
            WebUtil.redirectToList(resp, "用户名已存在");
        } catch (RuntimeException e) {
            log("修改会员失败，id = " + id + "，username = " + member.getUname(), e);
            WebUtil.redirectToList(resp, "修改失败，请稍后重试");
        }
    }
}
