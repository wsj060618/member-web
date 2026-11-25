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
 * 会员列表，支持按用户名模糊搜索。
 */
@WebServlet({"/member-list"})
public class MemberListServlet extends HttpServlet {

    private final MemberDao memberDao = new JdbcMemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        if (keyword != null) {
            keyword = keyword.trim();
        }

        List<Member> list = List.of();
        try {
            if (keyword == null || keyword.isEmpty()) {
                list = memberDao.findAll();
            } else {
                list = memberDao.findByName(keyword);
            }
        } catch (RuntimeException e) {
            log("查询会员列表失败，keyword = " + keyword, e);
            req.setAttribute("errorMessage", "查询会员数据失败，请稍后重试或联系管理员");
        }

        req.setAttribute("memberList", list);
        req.setAttribute("keyword", keyword);

        // 接收新增/修改/删除后重定向带回的提示
        String message = req.getParameter("message");
        if (message != null && !message.isBlank()) {
            req.setAttribute("message", message);
        }

        req.getRequestDispatcher("/member-list.jsp").forward(req, resp);
    }
}
