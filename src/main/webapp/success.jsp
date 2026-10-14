<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // 从 request 域中取出 RegisterServlet 转发过来的数据
    String username = (String) request.getAttribute("username");
    String email = (String) request.getAttribute("email");
    String phone = (String) request.getAttribute("phone");
    String gender = (String) request.getAttribute("gender");
    String hobby = (String) request.getAttribute("hobby");

    // 性别：male -> 男，female -> 女
    String genderText = "male".equals(gender) ? "男" : ("female".equals(gender) ? "女" : "未填写");
    // 爱好映射：表单英文 value -> 中文显示
    String hobbyText;
    if (hobby == null || hobby.trim().isEmpty()) {
        hobbyText = "未选择";
    } else {
        hobbyText = switch (hobby) {
            case "reading" -> "阅读";
            case "music" -> "音乐";
            case "traveling" -> "旅行";
            case "sports" -> "运动";
            case "coding" -> "编程";
            default -> hobby;   // 未知值原样显示
        };
    }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>注册成功 - 会员管理系统</title>
    <link rel="stylesheet" href="css/site.css">
    <link rel="stylesheet" href="css/success.css">
</head>
<body>
<header class="site-header">
    <h1>会员管理系统</h1>
</header>

<div class="container">
    <section class="card">
        <p class="success-tip"><span class="check">✓</span>注册成功</p>
        <p class="welcome-line">欢迎您，<%= username %>!</p>

        <table class="info-table">
            <tr><th>用户名</th><td><%= username %></td></tr>
            <tr><th>邮箱</th><td><%= email %></td></tr>
            <tr><th>手机</th><td><%= phone %></td></tr>
            <tr><th>性别</th><td><%= genderText %></td></tr>
            <tr><th>爱好</th><td><%= hobbyText %></td></tr>
        </table>

        <p class="go-login">
            <a class="btn-go-login" href="login.jsp">去登录</a>
        </p>
    </section>
</div>

<footer class="site-footer">
    <p>&copy; 2026 长沙工业学院 • 应用程序设计课程</p>
</footer>
</body>
</html>
