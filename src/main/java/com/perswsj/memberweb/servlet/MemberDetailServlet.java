package com.perswsj.memberweb.servlet;

import com.perswsj.memberweb.dao.JdbcMemberDao;
import com.perswsj.memberweb.dao.MemberDao;
import com.perswsj.memberweb.pojo.Member;
import com.perswsj.memberweb.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/detail")
public class MemberDetailServlet extends HttpServlet {

    private final MemberDao memberDao = new JdbcMemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = WebUtil.parseId(req.getParameter("id"));
        // 非数字、非正数 id 直接当查不到处理，避免 parseInt 抛异常
        Member member = id == null ? null : memberDao.findById(id);

        req.setAttribute("member", member);
        req.getRequestDispatcher("/detail.jsp").forward(req, resp);
    }
}
