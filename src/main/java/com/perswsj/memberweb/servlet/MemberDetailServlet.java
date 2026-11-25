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

@WebServlet("/detail")
public class MemberDetailServlet extends HttpServlet {

    private final MemberDao memberDao = new JdbcMemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        Member member = null;
        // 非数字 id 直接当查不到处理，避免 parseInt 抛异常
        if (idStr != null && idStr.matches("\\d+")) {
            member = memberDao.findById(Integer.parseInt(idStr));
        }

        req.setAttribute("member", member);
        req.getRequestDispatcher("/detail.jsp").forward(req, resp);
    }
}
