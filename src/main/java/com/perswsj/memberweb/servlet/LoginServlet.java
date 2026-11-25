package com.perswsj.memberweb.servlet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import jakarta.servlet.http.HttpServlet;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=utf-8");

        Map<String, String> login = MAPPER.readValue(req.getInputStream(), new TypeReference<Map<String, String>>() {});
        String username = login.get("username");
        String password = login.get("password");

        if ("admin".equals(username) && "123456".equals(password)) {
            req.getSession().setAttribute("username", username);
            MAPPER.writeValue(resp.getWriter(), Map.of("success", true, "message", "登录成功"));
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            MAPPER.writeValue(resp.getWriter(), Map.of("success", false, "message", "用户名或密码错误"));
        }
    }
}
