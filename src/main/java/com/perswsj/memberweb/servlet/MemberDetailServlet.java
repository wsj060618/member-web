package com.perswsj.memberweb.servlet;

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

    MemberDao memberDao = new MemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 获取并校验 id
        String idStr = req.getParameter("id");
        Member member = null;
        if (idStr != null && idStr.matches("\\d+")) {
            // 调用 MemberDao 的 findById 获取该会员信息
            member = memberDao.findById(Integer.parseInt(idStr));
        }

        // 将会员信息存入 request 域
        req.setAttribute("member", member);

        // 转发到 detail.jsp 页面
        req.getRequestDispatcher("/detail.jsp").forward(req, resp);
    }
}
