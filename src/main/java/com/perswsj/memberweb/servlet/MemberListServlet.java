package com.perswsj.memberweb.servlet;

import com.perswsj.memberweb.dao.JdbcMemberDao;
import com.perswsj.memberweb.dao.MemberDao;
import com.perswsj.memberweb.pojo.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * 会员列表：支持按用户名模糊搜索。
 * <p>
 * 控制层只依赖 {@link MemberDao} 接口：关键字为空查全部，不为空按用户名模糊查询；
 * SQL 与参数绑定全部在 DAO 内完成，本类不出现任何 SQL 片段。
 */
@WebServlet({"/member-list"})
public class MemberListServlet extends HttpServlet {

    /** 无状态 DAO，可安全地作为 Servlet 的成员变量共享；Connection 等资源绝不放这里 */
    private final MemberDao memberDao = new JdbcMemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取传过来的查询条件；先去掉首尾空格，保证查询条件与搜索框回显一致
        String keyword = req.getParameter("keyword");
        if (keyword != null) {
            keyword = keyword.trim();
        }

        // 条件不存在查所有，存在则按用户名模糊查询
        List<Member> list = List.of();
        try {
            if (keyword == null || keyword.isEmpty()) {
                list = memberDao.findAll();
            } else {
                list = memberDao.findByName(keyword);
            }
        } catch (RuntimeException e) {
            // SQL 异常只在控制台留日志，页面只显示友好提示，不输出 SQL、库名、账号等细节
            log("查询会员列表失败，keyword = " + keyword, e);
            req.setAttribute("errorMessage", "查询会员数据失败，请稍后重试或联系管理员");
        }

        // 会员信息和搜索词都存入 request 域（只在本次页面显示，不放 Session）
        req.setAttribute("memberList", list);
        req.setAttribute("keyword", keyword);

        // 转发到列表页面
        req.getRequestDispatcher("/member-list.jsp").forward(req, resp);
    }
}
