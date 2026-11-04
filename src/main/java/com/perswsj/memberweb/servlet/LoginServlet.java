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
    // 创建一个 ObjectMapper 对象用于 JSON 数据的解析和生成
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 设置响应内容类型
        resp.setContentType("application/json;charset=utf-8");

        // 使用 Jackson 解析请求体中的 JSON 数据
        Map<String, String> login = MAPPER.readValue(req.getInputStream(), new TypeReference<Map<String, String>>() {});
        String username = login.get("username");
        String password = login.get("password");

        // 验证用户名和密码
        if ("admin".equals(username) && "123456".equals(password)) {
            req.getSession().setAttribute("username", username);
            MAPPER.writeValue(resp.getWriter(), Map.of("success", true, "message", "登录成功"));
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            MAPPER.writeValue(resp.getWriter(), Map.of("success", false, "message", "用户名或密码错误"));
        }
    }
}
