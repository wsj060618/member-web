package com.perswsj.memberweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 处理会员注册请求
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 设置编码格式，防止中文乱码
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        // 获取页面输入的数据
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String gender = req.getParameter("gender");
        String hobby = req.getParameter("hobby");

        //  通过 request 的 setAttribute 方法将获取到的数据放到 request 域中
        req.setAttribute("username", username);
        req.setAttribute("email", email);
        req.setAttribute("phone", phone);
        req.setAttribute("gender", gender);
        req.setAttribute("hobby", hobby);

        // 通过 request 的 getRequestDispatcher 方法转发到 success.jsp 页面
        req.getRequestDispatcher("/success.jsp").forward(req, resp);
    }
}
