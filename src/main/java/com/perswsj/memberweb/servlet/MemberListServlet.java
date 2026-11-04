package com.perswsj.memberweb.servlet;

import com.perswsj.memberweb.dao.MemberDao;
import com.perswsj.memberweb.pojo.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/member-list")
public class MemberListServlet extends HttpServlet{
    MemberDao memberDao = new MemberDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取传过来的查询条件
        String keyword = req.getParameter("keyword");

        // 条件不存在查所有，存在则按条件查询
        List<Member> list;
        if (keyword == null || keyword.trim().isEmpty()) {
            list = memberDao.findAll();
        } else {
            list = memberDao.findByName(keyword);
        }

        // 会员信息和搜索词都存入request域
        req.setAttribute("memberList", list);
        req.setAttribute("keyword", keyword);

        // 转发到列表页面
        req.getRequestDispatcher("/member-list.jsp").forward(req, resp);
    }
}
