<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>会员详情 - 会员管理系统</title>
    <link rel="stylesheet" href="css/site.css">
    <link rel="stylesheet" href="css/detail.css">
</head>
<body>
<header class="site-header">
    <h1>会员管理系统</h1>
    <nav>
        <a href="main.html">首页</a>
        <a href="register.html">注册</a>
        <a href="login.jsp">登录</a>
        <a href="member-list">会员列表</a>
    </nav>
</header>
<div class="container">
    <section class="card">
        <h2>会员详情</h2>
        <c:choose>
            <c:when test="${empty member}">
                <p style="color:#909399;text-align:center;padding:24px 0;">未找到该会员，可能已被删除。</p>
            </c:when>
            <c:otherwise>
                <table class="data detail-table">
                    <tr><th>编号</th><td>${member.id}</td></tr>
                    <tr><th>用户名</th><td>${member.uname}</td></tr>
                    <tr><th>邮箱</th><td>${member.email}</td></tr>
                    <tr><th>手机</th><td>${member.phone}</td></tr>
                    <tr><th>性别</th><td>${member.sexText}</td></tr>
                    <tr><th>爱好</th><td>${member.hobbyText}</td></tr>
                </table>
            </c:otherwise>
        </c:choose>
        <p class="detail-actions">
            <a class="btn btn-back" href="member-list">返回列表</a>
        </p>

    </section>
</div>
<footer class="site-footer">
    <p>&copy; 2026 长沙工业学院 • 应用程序设计课程</p>
</footer>
</body>
</html>